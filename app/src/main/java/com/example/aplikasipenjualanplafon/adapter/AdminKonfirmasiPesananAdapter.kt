package com.example.aplikasipenjualanplafon.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.KonfirmasiPesananModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.UsersModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminKonfirmasiPesananBinding
import com.example.aplikasipenjualanplafon.databinding.ListAdminSemuaUserBinding
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu

class AdminKonfirmasiPesananAdapter(
    private var listKonfirmasiPesanan: ArrayList<KonfirmasiPesananModel>,
    private var onClick: OnClickItem.ClickAkun
): RecyclerView.Adapter<AdminKonfirmasiPesananAdapter.ViewHolder>() {
    private val rupiah: KonversiRupiah = KonversiRupiah()
    private val tanggalDanWaktu: TanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListAdminKonfirmasiPesananBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminKonfirmasiPesananBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listKonfirmasiPesanan.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val konfirmasiPesanan = listKonfirmasiPesanan[position]
        val arrayTanggalDanWaktu = konfirmasiPesanan.waktu!!.split(" ")
        val tanggal = tanggalDanWaktu.konversiBulan(arrayTanggalDanWaktu[0])
        val waktu = tanggalDanWaktu.waktuNoSecond(arrayTanggalDanWaktu[1])
        holder.binding.apply {
            lsTvJumlahJenisPlafon.text = konfirmasiPesanan.jumlah_jenis_plafon
            lsTvTotalHargaPlafon.text = rupiah.rupiah(konfirmasiPesanan.total_harga!!.trim().toLong())
            lsTvTanggalWaktu.text = tanggal
//            lsTvWaktu.text = waktu
        }
    }
}