package com.example.aplikasipenjualanplafon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.databinding.ListPembayaranPesananBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class PembayaranPesananAdapter(
    private val listPlafon: ArrayList<PesananModel>
): RecyclerView.Adapter<PembayaranPesananAdapter.ViewHolder>() {
    private val rupiah = KonversiRupiah()
    class ViewHolder(val binding: ListPembayaranPesananBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPembayaranPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPlafon.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPesanan = listPlafon[position]

        holder.binding.apply {
            val hargaTotal = dataPesanan.jumlah!!.toInt() * dataPesanan.plafon!![0].harga!!.toInt()
            tvJenisPlafon.text = dataPesanan.plafon[0].jenis_plafon!![0].jenis_plafon
            tvPanjangKaliLebar.text = dataPesanan.plafon[0].ukuran
            tvHarga.text = rupiah.rupiah(hargaTotal.toLong())
//            tvPanjangKaliLebar.text = "${dataPesanan.panjang} X ${dataPesanan.lebar} m2"

        }
    }
}