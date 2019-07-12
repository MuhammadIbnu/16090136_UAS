package com.Ibnu.seminar.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Peserta(
    @SerializedName("title") var title : String?,
    @SerializedName("message") var message : String?,
    @SerializedName("input1") var input1 : String?,
    @SerializedName("input2") var input2 : String?,
    @SerializedName("id") var id : Int?,
    @SerializedName("date") var date : String?,
    @SerializedName("name_user") var name_user : String?,
    @SerializedName("email") var email : String?
) : Parcelable{
    constructor() : this(null, null,null, null, null, null, null, null)
}