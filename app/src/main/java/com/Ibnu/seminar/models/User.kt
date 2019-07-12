package com.Ibnu.seminar.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id : Int?,
    @SerializedName("name") var name : String?,
    @SerializedName("api_token") var api_token : String?,
    @SerializedName("email") var email : String?
) {
    constructor() : this(null, null, null, null)
}