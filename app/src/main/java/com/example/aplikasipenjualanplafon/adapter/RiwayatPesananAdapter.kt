package com.example.aplikasipenjualanplafon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananHalModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminKonfirmasiPesananBinding
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu

class RiwayatPesananAdapter(
    private var listPesanan: ArrayList<RiwayatPesananHalModel>,
    private var onClick: OnClickItem.ClickRiwayatPesanan
): RecyclerView.Adapter<RiwayatPesananAdapter.ViewHolder>() {
    private val tanggalDanWaktu = TanggalDanWaktu()
    private val rupiah = KonversiRupiah()
    class ViewHolder(var binding: ListAdminKonfirmasiPesananBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListAdminKonfirmasiPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPesanan.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val riwayatPesanan = listPesanan[position]
        holder.binding.apply {
            val arrayTanggalDanWaktu = riwayatPesanan.waktu!!.split(" ")
            val tanggal = tanggalDanWaktu.konversiBulan(arrayTanggalDanWaktu[0])
            val arrayWaktu = arrayTanggalDanWaktu[1].split(":")
            val waktu = "${arrayWaktu[0]}:${arrayWaktu[1]}"
            lsTvJumlahJenisPlafon.text = "${riwayatPesanan.jumlah_jenis_plafon} Jenis Plafon"
            lsTvTotalHargaPlafon.text = rupiah.rupiah(riwayatPesanan.total_harga!!.trim().toLong())
            lsTvTanggalWaktu.text = "$tanggal - $waktu"

            if(riwayatPesanan.ket=="0"){
                holder.itemView.setBackgroundResource(R.drawable.bg_card_failure)
            } else if(riwayatPesanan.ket=="1"){
                holder.itemView.setBackgroundResource(R.drawable.bg_card_success)
            }
        }

        holder.itemView.setOnClickListener {
            onClick.clickItem(riwayatPesanan.idPemesanan!!, it)
        }
    }


}