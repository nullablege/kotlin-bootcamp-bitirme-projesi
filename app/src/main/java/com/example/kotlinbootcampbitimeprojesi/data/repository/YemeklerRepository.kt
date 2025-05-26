import com.example.kotlinbootcampbitimeprojesi.data.model.CRUDCevap
import com.example.kotlinbootcampbitimeprojesi.data.model.SepetYemek
import com.example.kotlinbootcampbitimeprojesi.data.model.Yemek
import com.example.kotlinbootcampbitimeprojesi.data.network.YemeklerApiService

class YemeklerRepository(private val apiService: YemeklerApiService) {

    suspend fun tumYemekleriGetirRepo(): List<Yemek>? {
        return try {
            val response = apiService.tumYemekleriGetir()
            if (response.success == 1) {
                response.yemekler
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Sepete yemek ekleyen fonksiyon
    suspend fun sepeteYemekEkleRepo(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ): CRUDCevap? {
        return try {
            apiService.sepeteYemekEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun sepettekiYemekleriGetirRepo(kullanici_adi: String): List<SepetYemek>? {
        return try {
            val response = apiService.sepettekiYemekleriGetir(kullanici_adi)
            if (response.success == 1) {
                response.sepet_yemekler
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun sepettenYemekSilRepo(sepet_yemek_id: Int, kullanici_adi: String): CRUDCevap? {
        return try {
            apiService.sepettenYemekSil(sepet_yemek_id, kullanici_adi)
        } catch (e: Exception) {
            null
        }
    }


}