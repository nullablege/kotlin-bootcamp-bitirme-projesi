package com.example.kotlinbootcampbitimeprojesi.ui.viewmodel // Kendi paket adın

import YemeklerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SepetViewModelFactory(private val yemeklerRepository: YemeklerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SepetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SepetViewModel(yemeklerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}