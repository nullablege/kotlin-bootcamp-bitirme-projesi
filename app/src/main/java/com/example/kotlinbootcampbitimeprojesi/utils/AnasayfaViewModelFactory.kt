import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinbootcampbitimeprojesi.ui.viewmodel.AnasayfaViewModel

class AnasayfaViewModelFactory(private val yemeklerRepository: YemeklerRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnasayfaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Tür dönüşümünün güvenli olduğunu biliyoruz
            return AnasayfaViewModel(yemeklerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}