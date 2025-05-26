import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class DetayViewModelFactory(private val yemeklerRepository: YemeklerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetayViewModel(yemeklerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}