package com.Ibnu.seminar

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import com.Ibnu.seminar.adapters.PesertaAdapter
import com.Ibnu.seminar.models.Peserta
import com.Ibnu.seminar.networks.BaseListResponse
import com.Ibnu.seminar.services.ApiUtils
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.content_show.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowActivity : AppCompatActivity() {

    private var pesertas = mutableListOf<Peserta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        rv_peserta.layoutManager = LinearLayoutManager(this@ShowActivity)
        showData()
    }

    private fun showData(){
        pesertas.clear()
        val api = ApiUtils.getApiService()
        val req = api.all("Bearer ${getToken()}")
        req.enqueue(object : Callback<BaseListResponse<Peserta>>{
            override fun onFailure(call: Call<BaseListResponse<Peserta>>, t: Throwable) {
                Toast.makeText(this@ShowActivity, "Error with ${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<BaseListResponse<Peserta>>, response: Response<BaseListResponse<Peserta>>) {
                if(response.isSuccessful){
                    val b = response.body()
                    if(b != null && b.status){
                        val p = b.data as MutableList
                        rv_peserta.adapter = PesertaAdapter(p, this@ShowActivity)
                    }
                }else{
                    Toast.makeText(this@ShowActivity, "Kesalahan", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("API_TOKEN", "UNDEFINED")
        return token
    }

}
