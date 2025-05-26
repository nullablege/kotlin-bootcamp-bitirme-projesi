package com.example.kotlinbootcampbitimeprojesi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinbootcampbitimeprojesi.R
import com.example.kotlinbootcampbitimeprojesi.data.model.SepetYemek
import com.example.kotlinbootcampbitimeprojesi.data.network.RetrofitClient
import com.example.kotlinbootcampbitimeprojesi.databinding.ItemSepetYemekBinding // XML dosyanızın adı buysa

class SepetYemeklerAdapter(
    private var sepetListesi: List<SepetYemek>,
    private val onSilClicked: (SepetYemek) -> Unit
) : RecyclerView.Adapter<SepetYemeklerAdapter.SepetYemekViewHolder>() {

    inner class SepetYemekViewHolder(val binding: ItemSepetYemekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sepetYemek: SepetYemek) {
            binding.textViewSepetYemekAdi.text = sepetYemek.yemek_adi

            val fiyat = sepetYemek.yemek_fiyat.toDoubleOrNull() ?: 0.0
            val adet = sepetYemek.yemek_siparis_adet.toIntOrNull() ?: 0

            binding.textViewSepetYemekFiyat.text = "Fiyat: ₺${String.format("%.2f", fiyat)}"
            binding.textViewSepetYemekAdet.text = "Adet: $adet"
            binding.textViewSepetUrunToplamFiyat.text = "₺${String.format("%.2f", fiyat * adet)}"

            val tamResimUrl = RetrofitClient.BASE_IMAGE_URL + sepetYemek.yemek_resim_adi
            Glide.with(binding.root.context)
                .load(tamResimUrl)
                .error(R.drawable.error_image)
                .into(binding.imageViewSepetYemekResmi)

            binding.buttonSepettenSil.setOnClickListener {
                onSilClicked(sepetYemek)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetYemekViewHolder {
        val binding = ItemSepetYemekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SepetYemekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SepetYemekViewHolder, position: Int) {
        holder.bind(sepetListesi[position])
    }

    override fun getItemCount(): Int {
        return sepetListesi.size
    }

    fun updateSepetListesi(yeniListe: List<SepetYemek>) {
        sepetListesi = yeniListe
        notifyDataSetChanged()
    }
}