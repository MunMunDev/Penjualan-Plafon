package com.example.aplikasipenjualanplafon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminPesananListBinding
import com.example.aplikasipenjualanplafon.databinding.ListAdminRiwayatPesananBinding
import com.example.aplikasipenjualanplafon.databinding.ListAdminRiwayatPesananListBinding
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu

class AdminRiwayatPesananListAdapter(
    private var arrayListPesanan: ArrayList<ListPesananModel>,
    private var onClickItem: OnClickItem.ClickAdminRiwayatPesanan
): RecyclerView.Adapter<AdminRiwayatPesananListAdapter.ViewHolder>() {
    private var konversiRupiah: KonversiRupiah = KonversiRupiah()
    private var tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListAdminRiwayatPesananListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminRiwayatPesananListBinding.inflate(
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
            tvJumlahJenisPlafon.text = "${listPesanan.jumlah_jenis_plafon} Pesanan"
            tvHarga.text = konversiRupiah.rupiah(listPesanan.total_harga!!.toLong())
//            val arrayTanggalDanWaktu = listPesanan.waktu!!.split(" ")
//            val tanggal = tanggalDanWaktu.konversiBulan(arrayTanggalDanWaktu[0])
//            val waktu = tanggalDanWaktu.waktuNoSecond(arrayTanggalDanWaktu[1])
//            val valueTanggalDanWaktu = "$tanggal $waktu"
//            tvTanggal.text = valueTanggalDanWaktu

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
                onClickItem.clickPesanan(
                    listPesanan.id_user!!, listPesanan.nama!!,
                    listPesanan.jumlah_jenis_plafon!!.trim().toInt(), it
                )
                
//                if (listPesanan.jumlah_jenis_plafon!!.trim().toInt() > 0){
//                    onClickItem.clickPesanan(listPesanan.id_user!!, listPesanan.nama!!, it)
//                } else{
//                    Toast.makeText(holder.itemView.context, "Tidak ada data", Toast.LENGTH_SHORT).show()
//                }
            }
            holder.itemView.setOnLongClickListener {
//                onClickItem.clickItemSetting(listPesanan.idPemesanan!!, listPesanan.nama!!, it)
                true
            }

        }
    }
}