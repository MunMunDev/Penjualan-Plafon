package com.example.aplikasipenjualanplafon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.databinding.ListDataPlafonBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class PlafonAdapter(
    private val listPlafon: ArrayList<PlafonModel>,
    private val click: OnClickItem.ClickPlafon
): RecyclerView.Adapter<PlafonAdapter.ViewHolder>() {
    private val rupiah = KonversiRupiah()

    class ViewHolder(val binding: ListDataPlafonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDataPlafonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPlafon.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPlafon = listPlafon[position]

        holder.binding.apply {
            tvJudulPlafon.text = dataPlafon.jenis_plafon!![0].jenis_plafon
            tvStok.text = dataPlafon.stok
            tvUkuran.text = dataPlafon.ukuran
            tvHarga.text = rupiah.rupiah(dataPlafon.harga!!.trim().toLong())
            btnPesan.setOnClickListener {
                click.clickItemPlafon(dataPlafon, it)
            }

            ivGambarPlafon.setOnClickListener {
                click.clickItemImage(
                    jenisPlafon = dataPlafon.jenis_plafon[0].jenis_plafon!!,
                    image = "${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${dataPlafon.gambar}"
                )
            }

            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${dataPlafon.gambar}") // URL Gambar
                .error(R.drawable.gambar_not_have_image)
                .into(ivGambarPlafon) // imageView mana yang akan diterapkan
        }
    }
}