package com.example.kotlinbootcampbitimeprojesi.data.model

import com.google.gson.annotations.SerializedName

data class CRUDCevap(
    @SerializedName("success")
    var success:Int,
    @SerializedName("message")
    var message:String?
)