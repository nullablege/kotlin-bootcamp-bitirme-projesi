package com.example.kotlinbootcampbitimeprojesi.data.model

import com.google.gson.annotations.SerializedName

data class SepetYemeklerCevap(
    @SerializedName("sepet_yemekler") // API'den gelen JSON'daki key "sepet_yemekler" olmalı
    val sepet_yemekler: List<SepetYemek>, // Property adı "sepet_yemekler" ve tipi List<SepetYemek> olmalı
    @SerializedName("success")
    val success: Int
)