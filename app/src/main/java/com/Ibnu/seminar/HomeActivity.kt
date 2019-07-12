package com.Ibnu.seminar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Ibnu.seminar.models.Peserta
import com.Ibnu.seminar.networks.BaseResponse
import com.Ibnu.seminar.others.LoginActivity
import com.Ibnu.seminar.services.ApiUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private var apiService = ApiUtils.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        isLoggedIn()
        save()
        btn_show.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ShowActivity::class.java))
        }
    }

    private fun isLoggedIn() {
        val settings = getSharedPreferences("USER", Context.MODE_PRIVATE)
        val token = settings.getString("API_TOKEN", "UNDEFINED")
        if (token == null || token == "UNDEFINED") {
            Toast.makeText(this@HomeActivity, "isLogged", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun save(){
        btn_save.setOnClickListener {
            btn_save.isEnabled = false
            btn_show.isEnabled = false
            val nik = et_nik.text.toString().trim()
            val name = et_name.text.toString().trim()
            val address = et_address.text.toString().trim()
            val jk = sp_jk.selectedItem.toString()
            if(nik.isNotEmpty() && name.isNotEmpty() && address.isNotEmpty()){
                var req = apiService.insert("Bearer ${getToken()}", nik, name, address, jk)
                req.enqueue(object : Callback<BaseResponse<Peserta>>{
                    override fun onFailure(call: Call<BaseResponse<Peserta>>, t: Throwable) {
                        btn_save.isEnabled = true
                        btn_show.isEnabled = true
                        Toast.makeText(this@HomeActivity, "Error with ${t.message}", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<BaseResponse<Peserta>>, response: Response<BaseResponse<Peserta>>) {
                        if(response.isSuccessful){
                            val b = response.body()
                            if(b != null && b.status){
                                btn_save.isEnabled = true
                                btn_show.isEnabled = true
                                Toast.makeText(this@HomeActivity, "Berhasil disimpan", Toast.LENGTH_LONG).show()
                            }else{
                                btn_save.isEnabled = true
                                btn_show.isEnabled = true
                                Toast.makeText(this@HomeActivity, "Cannot insert", Toast.LENGTH_LONG).show()
                            }

                        }else{
                            Toast.makeText(this@HomeActivity, "Kesalahan", Toast.LENGTH_LONG).show()
                            btn_save.isEnabled = true
                            btn_show.isEnabled = true
                        }
                        btn_save.isEnabled = true
                        btn_show.isEnabled = true
                    }
                })
            }else{
                btn_save.isEnabled = true
                btn_show.isEnabled = true
            }
        }
    }

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("API_TOKEN", "UNDEFINED")
        return token
    }

}
