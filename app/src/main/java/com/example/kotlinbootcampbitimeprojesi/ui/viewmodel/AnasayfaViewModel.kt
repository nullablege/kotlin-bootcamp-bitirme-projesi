package com.example.kotlinbootcampbitimeprojesi.ui.viewmodel

import Event
import YemeklerRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinbootcampbitimeprojesi.data.model.CRUDCevap
import com.example.kotlinbootcampbitimeprojesi.data.model.Yemek
import com.example.kotlinbootcampbitimeprojesi.utils.Constants
import kotlinx.coroutines.launch
import java.util.Locale

class AnasayfaViewModel(private val yemeklerRepository: YemeklerRepository) : ViewModel() {

    private var tumYemeklerOrijinalListe: List<Yemek> = emptyList()

    private val _yemekListesi = MutableLiveData<List<Yemek>?>(emptyList())
    val yemekListesi: LiveData<List<Yemek>?> get() = _yemekListesi

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _hataMesaji = MutableLiveData<String?>()
    val hataMesaji: LiveData<String?> get() = _hataMesaji

    private val _hizliEklemeBasarili = MutableLiveData<Event<Boolean>>()
    val hizliEklemeBasarili: LiveData<Event<Boolean>> get() = _hizliEklemeBasarili

    private val _hizliEklemeHata = MutableLiveData<Event<String>>()
    val hizliEklemeHata: LiveData<Event<String>> get() = _hizliEklemeHata

    init {
        tumYemekleriYukle()
    }

    fun tumYemekleriYukle() {
        viewModelScope.launch {
            _isLoading.value = true
            _hataMesaji.value = null
            try {
                val liste = yemeklerRepository.tumYemekleriGetirRepo()
                if (liste != null) {
                    tumYemeklerOrijinalListe = liste
                    _yemekListesi.value = liste
                } else {
                    tumYemeklerOrijinalListe = emptyList()
                    _yemekListesi.value = emptyList()
                    _hataMesaji.value = "Yemekler yüklenirken bir sorun oluştu veya liste boş."
                }
            } catch (e: Exception) {
                tumYemeklerOrijinalListe = emptyList()
                _yemekListesi.value = emptyList()
                _hataMesaji.value = "Bir hata oluştu: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun yemekAra(aramaSorgusu: String) {
        val sorgu = aramaSorgusu.trim().lowercase(Locale.getDefault())

        if (sorgu.isEmpty()) {
            _yemekListesi.value = tumYemeklerOrijinalListe
        } else {
            val filtrelenmisListe = tumYemeklerOrijinalListe.filter { yemek ->
                yemek.yemek_adi.lowercase(Locale.getDefault()).contains(sorgu)
            }
            _yemekListesi.value = filtrelenmisListe
        }
    }

    fun hizliSepeteEkle(yemek: Yemek) {
        viewModelScope.launch {
            try {
                val cevap: CRUDCevap? = yemeklerRepository.sepeteYemekEkleRepo(
                    yemek_adi = yemek.yemek_adi,
                    yemek_resim_adi = yemek.yemek_resim_adi,
                    yemek_fiyat = yemek.yemek_fiyat.toIntOrNull() ?: 0,
                    yemek_siparis_adet = 1,
                    kullanici_adi = Constants.KULLANICI_ADI
                )
                if (cevap != null && cevap.success == 1) {
                    _hizliEklemeBasarili.value = Event(true)
                } else {
                    _hizliEklemeBasarili.value = Event(false)
                    _hizliEklemeHata.value = Event(cevap?.message ?: "Ürün sepete eklenemedi.")
                }
            } catch (e: Exception) {
                _hizliEklemeBasarili.value = Event(false)
                _hizliEklemeHata.value = Event("Hata: ${e.localizedMessage}")
            }
        }
    }
}