package com.Ibnu.seminar.services

import com.Ibnu.seminar.models.Peserta
import com.Ibnu.seminar.models.User
import com.Ibnu.seminar.networks.BaseListResponse
import com.Ibnu.seminar.networks.BaseResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("api/user/login")
    fun login(@Field("email") email : String, @Field("password") password : String) : Call<BaseResponse<User>>

    @FormUrlEncoded
    @POST("api/user/register")
    fun register(@Field("name") n : String, @Field("email") e : String,@Field("password") p : String) : Call<BaseResponse<User>>

    @FormUrlEncoded
    @POST("api/user/complaint")
    fun insert(@Header("Authorization") api_token: String, @Field("title") title : String,
               @Field("message") message : String,
               @Field("input1") input1 : String,
               @Field("input2") input2  : String) : Call<BaseResponse<Peserta>>


    @GET("api/user/complaint")
    fun all(@Header("Authorization") api_token : String) : Call<BaseListResponse<Peserta>>


}