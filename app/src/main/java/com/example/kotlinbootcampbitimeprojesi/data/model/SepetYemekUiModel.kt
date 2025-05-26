package com.example.kotlinbootcampbitimeprojesi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SepetYemekUiModel(
    val yemek_id_db_list: List<Int>,
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: String,
    var toplam_siparis_adet: Int,
    val kullanici_adi: String,
    val birim_fiyat_double: Double,
    var toplam_fiyat_double: Double
) : Parcelable