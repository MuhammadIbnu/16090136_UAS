package com.Ibnu.seminar.others

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.Ibnu.seminar.R
import com.Ibnu.seminar.models.User
import com.Ibnu.seminar.networks.BaseResponse
import com.Ibnu.seminar.services.ApiUtils

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var apiService = ApiUtils.getApiService()
    private var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        btn_back.setOnClickListener { finish() }
        register()
    }

    private fun register(){
        btn_register.setOnClickListener {
            btn_register.isEnabled = false
            val __name = et_name.text.toString().trim()
            val __email = et_email.text.toString().trim()
            val __pass = et_pass.text.toString().trim()
            if(__name.isNotEmpty() && __email.isNotEmpty() && __pass.isNotEmpty()){
                loading.isIndeterminate = true
                val req = apiService.register(__name, __email, __pass)
                req.enqueue(object : Callback<BaseResponse<User>>{
                    override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
                        println(t.message)
                        loading.isIndeterminate = false
                        loading.progress = 0
                        btn_register.isEnabled = true
                        Toast.makeText(this@RegisterActivity, "Cannot connect to the server", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<BaseResponse<User>>, response: Response<BaseResponse<User>>) {
                        if(response.isSuccessful){
                           val body = response.body()
                            if(body != null && body.status){
                                val user : User = body.data
                                setLoggedIn(user.api_token!!)
                            }else{
                                Toast.makeText(this@RegisterActivity, "Tidak dapat register", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this@RegisterActivity, "Failed to get response from server", Toast.LENGTH_LONG).show()
                        }
                        loading.isIndeterminate = false
                        loading.progress = 0
                        btn_register.isEnabled = true
                    }
                })
            }else{
                loading.isIndeterminate = false
                loading.progress = 0
                btn_register.isEnabled = true
                Toast.makeText(this@RegisterActivity, "Lengkapi semua form", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoggedIn(key: String) {
        val editor = sharedPreferences?.edit()
        editor?.putString("API_TOKEN", key)
        editor?.commit()
//        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
    }
}
