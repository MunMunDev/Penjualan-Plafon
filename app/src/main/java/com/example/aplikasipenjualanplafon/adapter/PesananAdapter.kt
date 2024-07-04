package com.example.aplikasipenjualanplafon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.databinding.ListPesananPlafonBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class PesananAdapter(
    private val listPlafon: ArrayList<PesananModel>,
    private val click: OnClickItem.ClickPesanan
): RecyclerView.Adapter<PesananAdapter.ViewHolder>() {
    private val rupiah = KonversiRupiah()
    class ViewHolder(val binding: ListPesananPlafonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPesananPlafonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPlafon.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPesanan = listPlafon[position]

        holder.binding.apply {
            tvJudulPlafon.text = dataPesanan.plafon!![0].jenis_plafon!![0].jenis_plafon
            tvHarga.text = rupiah.rupiah(dataPesanan.plafon[0].harga!!.trim().toLong())
            tvTotal.text = "${dataPesanan.jumlah}"

            ivGambarPlafon.setOnClickListener {
                click.clickGambarPesanan(
                    "${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${dataPesanan.plafon[0].gambar!!}",
                    dataPesanan.plafon[0].jenis_plafon!![0].jenis_plafon!!,
                    it
                )
            }

            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${dataPesanan.plafon[0].gambar}")
                .error(R.drawable.gambar_not_have_image)
                .into(ivGambarPlafon)
        }
        holder.itemView.setOnClickListener{
            click.clickItemPesanan(dataPesanan, it)
        }
    }
}