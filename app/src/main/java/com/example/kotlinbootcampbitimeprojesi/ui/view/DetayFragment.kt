package com.example.kotlinbootcampbitimeprojesi.ui.view

import DetayViewModel
import DetayViewModelFactory
import YemeklerRepository
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kotlinbootcampbitimeprojesi.R // Placeholder/Error için
import com.example.kotlinbootcampbitimeprojesi.data.model.Yemek // ÖNEMLİ: Bu import olmalı args için
import com.example.kotlinbootcampbitimeprojesi.data.network.RetrofitClient
import com.example.kotlinbootcampbitimeprojesi.databinding.FragmentDetayBinding
import com.example.kotlinbootcampbitimeprojesi.utils.Constants

class DetayFragment : Fragment() {
    private var _binding: FragmentDetayBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetayViewModel
    private val args: DetayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetayBinding.inflate(inflater, container, false)


        val yemeklerApiService = RetrofitClient.apiService
        val yemeklerRepository = YemeklerRepository(yemeklerApiService)
        val viewModelFactory = DetayViewModelFactory(yemeklerRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetayViewModel::class.java]

        viewModel.secilenYemegiAyarla(args.secilenYemek)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUIWithYemekData(args.secilenYemek)
        setupButtonClickListeners()
        observeViewModel()

        binding.toolbarDetay.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupUIWithYemekData(yemek: Yemek) {
        binding.textViewDetayYemekAdi.text = yemek.yemek_adi
        binding.textViewDetayFiyat.text = "₺${yemek.yemek_fiyat}"

        val tamResimUrl = RetrofitClient.BASE_IMAGE_URL + yemek.yemek_resim_adi
        Glide.with(this)
            .load(tamResimUrl)
            .error(R.drawable.error_image)
            .into(binding.imageViewDetayYemek)

        binding.toolbarDetay.title = yemek.yemek_adi
    }

    private fun setupButtonClickListeners() {
        binding.buttonArtir.setOnClickListener {
            viewModel.adetiArtir()
        }
        binding.buttonAzalt.setOnClickListener {
            viewModel.adetiAzalt()
        }
        binding.buttonDetaySepeteEkle.setOnClickListener {
            viewModel.sepeteEkle(Constants.KULLANICI_ADI)
        }
    }

    private fun observeViewModel() {
        viewModel.adet.observe(viewLifecycleOwner) { adet ->
            binding.textViewAdet.text = adet.toString()

        }

        viewModel.sepeteEklemeSonucu.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { basariliMi ->
                if (basariliMi) {
                    Toast.makeText(requireContext(), "Ürün sepete eklendi!", Toast.LENGTH_SHORT).show()

                }
            }
        }

        viewModel.sepeteEklemeHataMesaji.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { hataMesaji ->
                Toast.makeText(requireContext(), hataMesaji, Toast.LENGTH_LONG).show()
                Log.e("DetayFragment", "Sepete Ekleme Hatası: $hataMesaji")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}