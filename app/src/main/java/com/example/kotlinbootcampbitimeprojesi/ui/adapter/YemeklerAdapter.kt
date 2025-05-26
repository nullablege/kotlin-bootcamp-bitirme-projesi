package com.example.kotlinbootcampbitimeprojesi.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinbootcampbitimeprojesi.R // Kendi R sınıfın
import com.example.kotlinbootcampbitimeprojesi.data.model.Yemek
import com.example.kotlinbootcampbitimeprojesi.data.network.RetrofitClient
import com.example.kotlinbootcampbitimeprojesi.databinding.ItemYemekCardBinding
import com.example.kotlinbootcampbitimeprojesi.ui.view.AnasayfaFragmentDirections

class YemeklerAdapter(
    private var yemekListesi: List<Yemek>,
    private val onYemekCardClicked: (Yemek) -> Unit, // Tüm karta tıklama için
    private val onAddToCartClicked: (Yemek) -> Unit  // "+" butonuna tıklama için
) : RecyclerView.Adapter<YemeklerAdapter.YemekViewHolder>() {

    inner class YemekViewHolder(val binding: ItemYemekCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(yemek: Yemek) {
            binding.textViewYemekAdi.text = yemek.yemek_adi
            binding.textViewYemekFiyat.text = "₺${yemek.yemek_fiyat}" // Fiyatın String olduğunu varsayıyoruz

            val tamResimUrl = RetrofitClient.BASE_IMAGE_URL + yemek.yemek_resim_adi
            Glide.with(binding.root.context)
                .load(tamResimUrl)
                .error(R.drawable.error_image)       // Kendi error'unu ekle
                .into(binding.imageViewYemek)

            binding.root.setOnClickListener {
                onYemekCardClicked(yemek)
            }

            binding.buttonSepeteEkle.setOnClickListener {
                Log.d("YemeklerAdapter", "${yemek.yemek_adi} için '+' (sepete ekle) butonuna tıklandı.")
                onAddToCartClicked(yemek)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemYemekCardBinding.inflate(inflater, parent, false)
        return YemekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YemekViewHolder, position: Int) {
        holder.bind(yemekListesi[position])
    }

    override fun getItemCount(): Int {
        return yemekListesi.size
    }

    fun updateYemekler(yeniListe: List<Yemek>) {
        yemekListesi = yeniListe
        notifyDataSetChanged()
    }
}