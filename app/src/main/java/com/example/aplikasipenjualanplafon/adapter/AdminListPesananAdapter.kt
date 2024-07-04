package com.example.aplikasipenjualanplafon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminPesananListBinding
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu

class AdminListPesananAdapter(
    private var arrayListPesanan: ArrayList<ListPesananModel>,
    private var onClickItem: OnClickItem.ClickAdminPesanan
): RecyclerView.Adapter<AdminListPesananAdapter.ViewHolder>() {
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

//            if(listPesanan.ket=="0"){
//                holder.itemView.setBackgroundResource(R.drawable.bg_card_failure)
//            } else if(listPesanan.ket=="1"){
//                holder.itemView.setBackgroundResource(R.drawable.bg_card_success)
//            }

            if(listPesanan.ket == "0"){
                holder.itemView.setBackgroundResource(R.drawable.bg_card_failure)
            } else if(listPesanan.ket == "1"){
                holder.itemView.setBackgroundResource(R.drawable.bg_card_success)
            }

            holder.itemView.setOnClickListener{
                onClickItem.clickPesanan(listPesanan.id_pemesanan!!, listPesanan.nama!!, it)
            }
            holder.itemView.setOnLongClickListener {
//                onClickItem.clickItemSetting(listPesanan.idPemesanan!!, listPesanan.nama!!, it)
                true
            }

        }
    }
}