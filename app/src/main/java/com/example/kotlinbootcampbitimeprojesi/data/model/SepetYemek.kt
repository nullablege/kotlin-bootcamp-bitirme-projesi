package com.example.kotlinbootcampbitimeprojesi.data.model

import com.google.gson.annotations.SerializedName

data class SepetYemek(
    @SerializedName("sepet_yemek_id") var sepet_yemek_id:String,
    @SerializedName("yemek_adi") var yemek_adi:String,
    @SerializedName("yemek_resim_adi") var yemek_resim_adi:String,
    @SerializedName("yemek_fiyat") var yemek_fiyat:String,
    @SerializedName("yemek_siparis_adet") var yemek_siparis_adet:String,
    @SerializedName("kullanici_adi") var kullanici_adi:String
)