package com.Ibnu.seminar.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiUtils {
    companion object {
        const val API_URL = "https://tablo-app.herokuapp.com/"
        private var retrofit : Retrofit? = null
        private var okHttp = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS).build()

        private fun getClient() : Retrofit {
            return if (retrofit == null){
                retrofit = Retrofit.Builder().baseUrl(API_URL).client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                retrofit!!
            }else{
                retrofit!!
            }
        }

        fun getApiService() : ApiServices = getClient()
            .create(ApiServices::class.java)
    }
}