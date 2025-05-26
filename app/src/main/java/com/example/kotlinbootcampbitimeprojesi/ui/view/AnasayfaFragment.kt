package com.example.kotlinbootcampbitimeprojesi.ui.view

import AnasayfaViewModelFactory
import YemeklerRepository
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinbootcampbitimeprojesi.R
import com.example.kotlinbootcampbitimeprojesi.data.network.RetrofitClient
import com.example.kotlinbootcampbitimeprojesi.databinding.FragmentAnasayfaBinding
import com.example.kotlinbootcampbitimeprojesi.ui.adapter.YemeklerAdapter
import com.example.kotlinbootcampbitimeprojesi.ui.viewmodel.AnasayfaViewModel
import com.example.kotlinbootcampbitimeprojesi.utils.Constants

class AnasayfaFragment : Fragment() {

    private var _binding: FragmentAnasayfaBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AnasayfaViewModel
    private lateinit var yemeklerAdapter: YemeklerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnasayfaBinding.inflate(inflater, container, false)

        val yemeklerApiService = RetrofitClient.apiService
        val yemeklerRepository = YemeklerRepository(yemeklerApiService)
        val viewModelFactory = AnasayfaViewModelFactory(yemeklerRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[AnasayfaViewModel::class.java]

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        yemeklerAdapter = YemeklerAdapter(
            yemekListesi = emptyList(),
            onYemekCardClicked = { secilenYemek ->
                try {
                    Log.d("NAV_DEBUG", "Yemek tıklandı: ${secilenYemek.yemek_adi}")

                    val action = AnasayfaFragmentDirections.actionAnasayfaFragmentToDetayFragment(secilenYemek)

                    Log.d("NAV_DEBUG", "Action oluşturuldu. ID: ${action.actionId}, Argümanlar: ${action.arguments}")
                    findNavController().navigate(action)
                    Log.d("NAV_DEBUG", "Navigasyon çağrıldı.")

                } catch (e: Exception) {
                    Log.e("NAV_DEBUG", "Navigasyon hatası: ${e.message}", e)
                    Toast.makeText(requireContext(), "Detay sayfasına ulaşılamadı: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            },
            onAddToCartClicked = { sepeteEklenecekYemek ->
                viewModel.hizliSepeteEkle(sepeteEklenecekYemek)
            }
        )
        binding.recyclerViewYemekler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = yemeklerAdapter
        }
    }

    private fun setupUIListeners() {
        binding.imageButtonSepet.setOnClickListener {
            try {
                val action = AnasayfaFragmentDirections.AnasayfadanSepete()
                findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e("AnasayfaFragment", "Sepete navigasyon hatası: ${e.localizedMessage}", e)
                Toast.makeText(requireContext(), "Sepet sayfasına gidilemedi.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchViewYemek.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.yemekAra(query.orEmpty())
                binding.searchViewYemek.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.yemekAra(newText.orEmpty())
                return true
            }
        })

        binding.searchViewYemek.setOnCloseListener {
            viewModel.yemekAra("")
            false
        }
    }

    private fun observeViewModel() {
        viewModel.yemekListesi.observe(viewLifecycleOwner) { yemekler ->
            yemekler?.let { list ->
                yemeklerAdapter.updateYemekler(list)
                val aramaSorgusu = binding.searchViewYemek.query.toString()
                val hataMesajiMevcut = viewModel.hataMesaji.value != null
                val yuklemeDevamEdiyor = viewModel.isLoading.value == true

                if (yuklemeDevamEdiyor) {
                    binding.recyclerViewYemekler.visibility = View.GONE
                    binding.textViewSepetBos.visibility = View.GONE
                } else if (hataMesajiMevcut) {
                    binding.recyclerViewYemekler.visibility = View.GONE
                    binding.textViewSepetBos.text = viewModel.hataMesaji.value
                    binding.textViewSepetBos.visibility = View.VISIBLE
                } else if (list.isEmpty()) {
                    binding.recyclerViewYemekler.visibility = View.GONE
                    binding.textViewSepetBos.text = if (aramaSorgusu.isNotEmpty()) "Aramanızla eşleşen yemek bulunamadı." else "Gösterilecek yemek bulunamadı."
                    binding.textViewSepetBos.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewYemekler.visibility = View.VISIBLE
                    binding.textViewSepetBos.visibility = View.GONE
                }
            } ?: run{
                if (viewModel.isLoading.value == true) {
                    binding.recyclerViewYemekler.visibility = View.GONE
                    binding.textViewSepetBos.visibility = View.GONE
                    binding.progressBarAnasayfa.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewYemekler.visibility = View.GONE
                    binding.textViewSepetBos.text = "Veri yüklenemedi."
                    binding.textViewSepetBos.visibility = View.VISIBLE
                    binding.progressBarAnasayfa.visibility = View.GONE
                }

            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarAnasayfa.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading){
                binding.recyclerViewYemekler.visibility = View.GONE
                binding.textViewSepetBos.visibility = View.GONE
            }
        }

        viewModel.hataMesaji.observe(viewLifecycleOwner) { hata ->

            hata?.let {
                if (viewModel.isLoading.value == false) { // Sadece yükleme bittiğinde hata toast'ı göster
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.hizliEklemeBasarili.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { basariliMi ->
                if (basariliMi) {
                    Toast.makeText(requireContext(), "Ürün sepete eklendi!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.hizliEklemeHata.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { hataMesaji ->
                Toast.makeText(requireContext(), hataMesaji, Toast.LENGTH_LONG).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}