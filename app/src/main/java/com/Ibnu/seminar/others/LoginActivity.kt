package com.Ibnu.seminar.others

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.Ibnu.seminar.models.User
import com.Ibnu.seminar.networks.BaseResponse
import com.Ibnu.seminar.services.ApiUtils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.content.SharedPreferences
import com.Ibnu.seminar.HomeActivity
import com.Ibnu.seminar.R

class LoginActivity : AppCompatActivity() {

    private var apiService = ApiUtils.getApiService()
    private var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        doLogin()
        btn_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            btn_login.isEnabled = false
            val email = et_email.text.toString().trim()
            val pass = et_pass.text.toString().trim()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                loading.isIndeterminate = true
                val req = apiService.login(email, pass)
                req.enqueue(object : Callback<BaseResponse<User>>{
                    override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
                        println("Duh error mas...")
                        Toast.makeText(this@LoginActivity, "Cannot connect to the server", Toast.LENGTH_LONG).show()
                        loading.isIndeterminate = false
                        loading.progress = 0
                        btn_login.isEnabled = true
                    }

                    override fun onResponse(call: Call<BaseResponse<User>>, response: Response<BaseResponse<User>>) {
                        if(response.isSuccessful){
                            val body = response.body()
                            if(body != null && body.status){
                                val u : User = body.data
                                setLoggedIn(u.api_token!!)
                            }else{ Toast.makeText(this@LoginActivity, "Tidak dapat masuk", Toast.LENGTH_LONG).show() }
                        }else{
                            Toast.makeText(this@LoginActivity, "Failed to get response from server", Toast.LENGTH_LONG).show()
                        }
                        btn_login.isEnabled = true
                        loading.isIndeterminate = false
                        loading.progress = 0
                    }
                })
            }else{
                btn_login.isEnabled = true
                loading.isIndeterminate = false
                loading.progress = 0
                Toast.makeText(this@LoginActivity, "Dih apa sih kak?!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoggedIn(key: String) {
        val editor = sharedPreferences?.edit()
        editor?.putString("API_TOKEN", key)
        editor?.commit()
        Toast.makeText(this@LoginActivity, "set logged in $key", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finish()
    }

    private fun isNotLoggedIn(): Boolean {
        val token = sharedPreferences?.getString("API_TOKEN", "UNDEFINED")
        return token == null || token == "UNDEFINED"
    }

    override fun onResume() {
        super.onResume()
        if(!isNotLoggedIn()){
            Toast.makeText(this@LoginActivity, "onResme", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }
}
