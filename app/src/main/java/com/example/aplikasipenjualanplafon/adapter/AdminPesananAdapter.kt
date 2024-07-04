package com.example.aplikasipenjualanplafon.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminPesananBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class AdminPesananAdapter(
    private var listPesanan: ArrayList<PesananModel>,
    private var onClick: OnClickItem.ClickAdminPesanan
): RecyclerView.Adapter<AdminPesananAdapter.ViewHolder>() {

    private val rupiah: KonversiRupiah = KonversiRupiah()

    class ViewHolder(val binding: ListAdminPesananBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminPesananBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listPesanan.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvNama.text = "Nama User"
                tvAlamat.text = "Alamat"
                tvNomorHp.text = "Nomor HP"
                tvJenisPlafon.text = "Jenis Plafon"
                tvHarga.text = "Harga (m2)"
                tvPanjang.text = "Panjang (m2)"
                tvLebar.text = "Lebar (m2)"
                tvTotalHarga.text = "Total Harga"
                tvWaktu.text = "Waktu"
                tvWaktuKonfirmasi.text = "Waktu Konfirmasi"
                tvMetodePembayaran.text = "Met.Pembayaran"
                tvWaktuBayar.text = "Pembayaran"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNama.setBackgroundResource(R.drawable.bg_table_title)
                tvAlamat.setBackgroundResource(R.drawable.bg_table_title)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisPlafon.setBackgroundResource(R.drawable.bg_table_title)
                tvHarga.setBackgroundResource(R.drawable.bg_table_title)
                tvPanjang.setBackgroundResource(R.drawable.bg_table_title)
                tvLebar.setBackgroundResource(R.drawable.bg_table_title)
                tvTotalHarga.setBackgroundResource(R.drawable.bg_table_title)
//                ivGambarPlafon.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktu.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktuKonfirmasi.setBackgroundResource(R.drawable.bg_table_title)
                tvMetodePembayaran.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktuBayar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNama.setTextColor(Color.parseColor("#ffffff"))
                tvAlamat.setTextColor(Color.parseColor("#ffffff"))
                tvNomorHp.setTextColor(Color.parseColor("#ffffff"))
                tvJenisPlafon.setTextColor(Color.parseColor("#ffffff"))
                tvHarga.setTextColor(Color.parseColor("#ffffff"))
                tvPanjang.setTextColor(Color.parseColor("#ffffff"))
                tvLebar.setTextColor(Color.parseColor("#ffffff"))
                tvTotalHarga.setTextColor(Color.parseColor("#ffffff"))
                tvWaktu.setTextColor(Color.parseColor("#ffffff"))
                tvWaktuKonfirmasi.setTextColor(Color.parseColor("#ffffff"))
                tvMetodePembayaran.setTextColor(Color.parseColor("#ffffff"))
                tvWaktuBayar.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNama.setTypeface(null, Typeface.BOLD)
                tvAlamat.setTypeface(null, Typeface.BOLD)
                tvNomorHp.setTypeface(null, Typeface.BOLD)
                tvJenisPlafon.setTypeface(null, Typeface.BOLD)
                tvHarga.setTypeface(null, Typeface.BOLD)
                tvPanjang.setTypeface(null, Typeface.BOLD)
                tvLebar.setTypeface(null, Typeface.BOLD)
                tvTotalHarga.setTypeface(null, Typeface.BOLD)
                tvWaktu.setTypeface(null, Typeface.BOLD)
                tvWaktuKonfirmasi.setTypeface(null, Typeface.BOLD)
                tvMetodePembayaran.setTypeface(null, Typeface.BOLD)
                tvWaktuBayar.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pesanan = listPesanan[(position-1)]

//                tvNo.text = "$position"
//                tvNama.text = pesanan.nama
//                tvAlamat.text = pesanan.alamat
//                tvNomorHp.text = pesanan.nomor_hp
//                tvJenisPlafon.text = pesanan.jenis_plafon
//                tvHarga.text = rupiah.rupiah(pesanan.harga!!.trim().toLong())
////                tvPanjang.text = "${pesanan.panjang} m2"
////                tvLebar.text = "${pesanan.lebar} m2"
//                tvTotalHarga.text = rupiah.rupiah(pesanan.total_harga!!.trim().toLong())
//                tvWaktu.text = pesanan.waktu
//                tvWaktuKonfirmasi.text = pesanan.waktu_konfirmasi
//                tvMetodePembayaran.text = pesanan.metode_pembayaran
//                tvWaktuBayar.text = pesanan.waktu_bayar
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNama.setBackgroundResource(R.drawable.bg_table)
                tvAlamat.setBackgroundResource(R.drawable.bg_table)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table)
                tvJenisPlafon.setBackgroundResource(R.drawable.bg_table)
                tvHarga.setBackgroundResource(R.drawable.bg_table)
                tvPanjang.setBackgroundResource(R.drawable.bg_table)
                tvLebar.setBackgroundResource(R.drawable.bg_table)
                tvTotalHarga.setBackgroundResource(R.drawable.bg_table)
//                ivGambarPlafon.setBackgroundResource(R.drawable.bg_table)
                tvWaktu.setBackgroundResource(R.drawable.bg_table)
                tvWaktuKonfirmasi.setBackgroundResource(R.drawable.bg_table)
                tvMetodePembayaran.setBackgroundResource(R.drawable.bg_table)
                tvWaktuBayar.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNama.setTextColor(Color.parseColor("#000000"))
                tvAlamat.setTextColor(Color.parseColor("#000000"))
                tvNomorHp.setTextColor(Color.parseColor("#000000"))
                tvJenisPlafon.setTextColor(Color.parseColor("#000000"))
                tvHarga.setTextColor(Color.parseColor("#000000"))
                tvPanjang.setTextColor(Color.parseColor("#000000"))
                tvLebar.setTextColor(Color.parseColor("#000000"))
                tvTotalHarga.setTextColor(Color.parseColor("#000000"))
                tvWaktu.setTextColor(Color.parseColor("#000000"))
                tvWaktuKonfirmasi.setTextColor(Color.parseColor("#000000"))
                tvMetodePembayaran.setTextColor(Color.parseColor("#000000"))
                tvWaktuBayar.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNama.setTypeface(null, Typeface.NORMAL)
                tvAlamat.setTypeface(null, Typeface.NORMAL)
                tvNomorHp.setTypeface(null, Typeface.NORMAL)
                tvJenisPlafon.setTypeface(null, Typeface.NORMAL)
                tvHarga.setTypeface(null, Typeface.NORMAL)
                tvPanjang.setTypeface(null, Typeface.NORMAL)
                tvLebar.setTypeface(null, Typeface.NORMAL)
                tvTotalHarga.setTypeface(null, Typeface.NORMAL)
                tvWaktu.setTypeface(null, Typeface.NORMAL)
                tvWaktuKonfirmasi.setTypeface(null, Typeface.NORMAL)
                tvMetodePembayaran.setTypeface(null, Typeface.NORMAL)
                tvWaktuBayar.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvAlamat.gravity = Gravity.CENTER_VERTICAL

//                Glide.with(holder.itemView)
//                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${pesanan.gambar}")
//                    .error(R.drawable.gambar_not_have_image)
//                    .into(ivGambarPlafon)

//                tvAlamat.setOnClickListener{
//                    onClick.clickAlamatPesanan(pesanan.alamat!!, it)
//                }
////                ivGambarPlafon.setOnClickListener {
////                    onClick.clickGambarPesanan(pesanan.gambar!!, pesanan.jenis_plafon!!, it)
////                }
//                tvSetting.setOnClickListener {
//                    onClick.clickItemSetting(pesanan, it)
//                }
            }
        }
    }
}