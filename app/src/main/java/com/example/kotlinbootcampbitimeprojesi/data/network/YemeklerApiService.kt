package com.example.kotlinbootcampbitimeprojesi.data.network

import com.example.kotlinbootcampbitimeprojesi.data.model.CRUDCevap
import com.example.kotlinbootcampbitimeprojesi.data.model.SepetYemeklerCevap
import com.example.kotlinbootcampbitimeprojesi.data.model.YemeklerCevap
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemeklerApiService {

    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir(): YemeklerCevap

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun sepeteYemekEkle(
        @Field("yemek_adi") yemek_adi: String,
        @Field("yemek_resim_adi") yemek_resim_adi: String,
        @Field("yemek_fiyat") yemek_fiyat: Int,
        @Field("yemek_siparis_adet") yemek_siparis_adet: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): CRUDCevap

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun sepettekiYemekleriGetir(
        @Field("kullanici_adi") kullanici_adi: String
    ): SepetYemeklerCevap

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun sepettenYemekSil(
        @Field("sepet_yemek_id") sepet_yemek_id: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): CRUDCevap
}