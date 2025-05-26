import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinbootcampbitimeprojesi.data.model.CRUDCevap
import com.example.kotlinbootcampbitimeprojesi.data.model.Yemek
import kotlinx.coroutines.launch

class DetayViewModel(private val yemeklerRepository: YemeklerRepository) : ViewModel() {

    private val _sepeteEklemeSonucu = MutableLiveData<Event<Boolean>>()
    val sepeteEklemeSonucu: LiveData<Event<Boolean>> get() = _sepeteEklemeSonucu

    private val _sepeteEklemeHataMesaji = MutableLiveData<Event<String>>()
    val sepeteEklemeHataMesaji: LiveData<Event<String>> get() = _sepeteEklemeHataMesaji

    private val _adet = MutableLiveData<Int>(1)
    val adet: LiveData<Int> get() = _adet



    private var _secilenYemek: Yemek? = null

    fun secilenYemegiAyarla(yemek: Yemek) {
        _secilenYemek = yemek
        _adet.value = 1
    }

    fun adetiArtir() {
        _adet.value = (_adet.value ?: 1) + 1
    }

    fun adetiAzalt() {
        val mevcutAdet = _adet.value ?: 1
        if (mevcutAdet > 1) {
            _adet.value = mevcutAdet - 1
        }
    }

    fun sepeteEkle(kullaniciAdi: String) {
        _secilenYemek?.let { yemek ->
            val siparisAdeti = _adet.value ?: 1
            viewModelScope.launch {
                try {
                    val cevap: CRUDCevap? = yemeklerRepository.sepeteYemekEkleRepo(
                        yemek_adi = yemek.yemek_adi,
                        yemek_resim_adi = yemek.yemek_resim_adi,
                        yemek_fiyat = yemek.yemek_fiyat.toIntOrNull() ?: 0,
                        yemek_siparis_adet = siparisAdeti,
                        kullanici_adi = kullaniciAdi
                    )
                    if (cevap != null && cevap.success == 1) {
                        _sepeteEklemeSonucu.value = Event(true)
                    } else {
                        _sepeteEklemeSonucu.value = Event(false)
                        _sepeteEklemeHataMesaji.value = Event(cevap?.message ?: "Sepete eklenirken bir sorun oluştu.")
                    }
                } catch (e: Exception) {
                    _sepeteEklemeSonucu.value = Event(false)
                    _sepeteEklemeHataMesaji.value = Event("Hata: ${e.localizedMessage}")
                }
            }
        } ?: run {
            _sepeteEklemeSonucu.value = Event(false)
            _sepeteEklemeHataMesaji.value = Event("Sepete eklenecek ürün bulunamadı.")
        }
    }
}

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}