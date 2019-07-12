package com.Ibnu.seminar.networks

import com.google.gson.annotations.SerializedName

data class BaseListResponse<News>(@SerializedName("message") var message : String, @SerializedName("status") var status : Boolean, @SerializedName("data") var data : List<News>)

data class BaseResponse<T>(@SerializedName("message") var message : String, @SerializedName("status") var status : Boolean, @SerializedName("data") var data : T)
