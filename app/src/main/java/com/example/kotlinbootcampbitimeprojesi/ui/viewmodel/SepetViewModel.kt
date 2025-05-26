package com.example.kotlinbootcampbitimeprojesi.ui.viewmodel // Kendi paket adın

import Event
import YemeklerRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.kotlinbootcampbitimeprojesi.data.model.CRUDCevap
import com.example.kotlinbootcampbitimeprojesi.data.model.SepetYemek
import com.example.kotlinbootcampbitimeprojesi.utils.Constants // Kullanıcı adı için
import kotlinx.coroutines.launch

class SepetViewModel(private val yemeklerRepository: YemeklerRepository) : ViewModel() {

    private val _sepetListesi = MutableLiveData<List<SepetYemek>?>()
    val sepetListesi: LiveData<List<SepetYemek>?> get() = _sepetListesi

    private val _toplamTutar = MutableLiveData<Double>(0.0)
    val toplamTutar: LiveData<Double> get() = _toplamTutar

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _hataMesaji = MutableLiveData<String?>()
    val hataMesaji: LiveData<String?> get() = _hataMesaji

    private val _silmeSonucu = MutableLiveData<Event<Boolean>>()
    val silmeSonucu: LiveData<Event<Boolean>> get() = _silmeSonucu

    private val _silmeHataMesaji = MutableLiveData<Event<String>>()
    val silmeHataMesaji: LiveData<Event<String>> get() = _silmeHataMesaji


    fun sepettekiYemekleriYukle() {
        viewModelScope.launch {
            _isLoading.value = true
            _hataMesaji.value = null
            try {
                val liste = yemeklerRepository.sepettekiYemekleriGetirRepo(com.example.kotlinbootcampbitimeprojesi.utils.Constants.KULLANICI_ADI)
                _sepetListesi.value = liste
                hesaplaToplamTutar(liste)
            } catch (e: Exception) {
                _sepetListesi.value = emptyList()
                hesaplaToplamTutar(emptyList())
                _hataMesaji.value = "Sepet yüklenirken hata: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun hesaplaToplamTutar(liste: List<SepetYemek>?) {
        var toplam = 0.0
        liste?.forEach { sepetYemek ->
            try {
                val fiyat = sepetYemek.yemek_fiyat.toDoubleOrNull() ?: 0.0
                val adet = sepetYemek.yemek_siparis_adet.toIntOrNull() ?: 0
                toplam += fiyat * adet
            } catch (e: NumberFormatException) {
                _hataMesaji.value = "${sepetYemek.yemek_adi} için fiyat veya adet formatı hatalı."
            }
        }
        _toplamTutar.value = toplam
    }

    fun sepettenYemekSil(sepetYemekId: Int) {
        viewModelScope.launch {
            try {
                val cevap: CRUDCevap? = yemeklerRepository.sepettenYemekSilRepo(
                    sepet_yemek_id = sepetYemekId,
                    kullanici_adi = Constants.KULLANICI_ADI
                )
                if (cevap != null && cevap.success == 1) {
                    _silmeSonucu.value = Event(true)
                    sepettekiYemekleriYukle()
                } else {
                    _silmeSonucu.value = Event(false)
                    _silmeHataMesaji.value = Event(cevap?.message ?: "Ürün sepetten silinirken bir sorun oluştu.")
                }
            } catch (e: Exception) {
                _silmeSonucu.value = Event(false)
                _silmeHataMesaji.value = Event("Silme hatası: ${e.localizedMessage}")
            }
        }
    }


    fun sepetiGuncelleIstegi() {
        sepettekiYemekleriYukle()
    }
}