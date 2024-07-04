package com.example.aplikasipenjualanplafon.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.ListKeranjangBelanjaModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminPesananListBinding
import com.example.aplikasipenjualanplafon.databinding.ListAdminPlafonBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu

class AdminKeranjangBelanjaAdapter(
    private var arrayListPesanan: ArrayList<ListKeranjangBelanjaModel>,
    private var onClickItem: OnClickItem.ClickAdminKeranjangBelanja
): RecyclerView.Adapter<AdminKeranjangBelanjaAdapter.ViewHolder>() {
    private var konversiRupiah: KonversiRupiah = KonversiRupiah()
    private var tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListAdminPesananListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminPesananListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (arrayListPesanan.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val listPesanan = arrayListPesanan[position]
            tvNama.text = listPesanan.nama
            tvJumlahJenisPlafon.text = "${listPesanan.jumlah_jenis_plafon} Jenis Plafon"
            tvHarga.text = konversiRupiah.rupiah(listPesanan.total_harga!!.toLong())
            val arrayTanggalDanWaktu = listPesanan.waktu!!.split(" ")
            val tanggal = tanggalDanWaktu.konversiBulan(arrayTanggalDanWaktu[0])
            val waktu = tanggalDanWaktu.waktuNoSecond(arrayTanggalDanWaktu[1])
            val valueTanggalDanWaktu = "$tanggal $waktu"
            tvTanggal.text = valueTanggalDanWaktu

            holder.itemView.setOnClickListener{
                onClickItem.clickPesanan(listPesanan.id_user!!, listPesanan.nama!!, it)
            }
            holder.itemView.setOnLongClickListener {
//                onClickItem.clickItemSetting(listPesanan.idPemesanan!!, listPesanan.nama!!, it)
                true
            }

        }
    }
}