package com.example.kotlinbootcampbitimeprojesi.ui.view

import YemeklerRepository
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinbootcampbitimeprojesi.R
import com.example.kotlinbootcampbitimeprojesi.data.network.RetrofitClient
import com.example.kotlinbootcampbitimeprojesi.databinding.FragmentSepetBinding
import com.example.kotlinbootcampbitimeprojesi.ui.adapter.SepetYemeklerAdapter
import com.example.kotlinbootcampbitimeprojesi.ui.viewmodel.SepetViewModel
import com.example.kotlinbootcampbitimeprojesi.ui.viewmodel.SepetViewModelFactory


class SepetFragment : Fragment() {
    private var _binding: FragmentSepetBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SepetViewModel
    private lateinit var sepetAdapter: SepetYemeklerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSepetBinding.inflate(inflater, container, false)

        val yemeklerApiService = RetrofitClient.apiService
        val yemeklerRepository = YemeklerRepository(yemeklerApiService)
        val viewModelFactory = SepetViewModelFactory(yemeklerRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[SepetViewModel::class.java]

        sepetAdapter = SepetYemeklerAdapter(emptyList()) { sepetYemek ->

            val idAsInt = sepetYemek.sepet_yemek_id.toIntOrNull()
            if (idAsInt != null) {
                viewModel.sepettenYemekSil(idAsInt)
            } else {
                Log.e("SepetFragment", "Geçersiz sepet_yemek_id: ${sepetYemek.sepet_yemek_id}")
                Toast.makeText(requireContext(), "Silme işlemi için ID hatalı.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerViewSepetUrunler.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSepetUrunler.adapter = sepetAdapter



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sepetListesi.observe(viewLifecycleOwner) { liste ->
            liste?.let {
                sepetAdapter.updateSepetListesi(it)
                binding.textViewSepetBos.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewModel.toplamTutar.observe(viewLifecycleOwner) { tutar ->
            binding.textViewToplamFiyatDeger.text = "₺${String.format("%.2f", tutar)}"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        }

        viewModel.hataMesaji.observe(viewLifecycleOwner) { hata ->
            hata?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.silmeSonucu.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { basariliMi ->
                if (basariliMi) {
                    Toast.makeText(requireContext(), "Ürün sepetten silindi.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.silmeHataMesaji.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { hataMesaji ->
                Toast.makeText(requireContext(), hataMesaji, Toast.LENGTH_LONG).show()
            }
        }

        binding.toolbarSepet.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSepetiOnayla.setOnClickListener {
            if (viewModel.sepetListesi.value.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Sepetiniz boş!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Siparişiniz onaylandı (simülasyon)!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.sepettekiYemekleriYukle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}