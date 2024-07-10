package com.example.aplikasipenjualanplafon.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminRiwayatPesananBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class AdminRiwayatPesananDetailAdapter(
    private var listPesanan: ArrayList<RiwayatPesananValModel>,
    private var onClick: OnClickItem.ClickAdminRiwayatPesananDetail
): RecyclerView.Adapter<AdminRiwayatPesananDetailAdapter.ViewHolder>() {

    private val rupiah: KonversiRupiah = KonversiRupiah()

    class ViewHolder(val binding: ListAdminRiwayatPesananBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminRiwayatPesananBinding.inflate(
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
                tvIdPemesanan.text = "ID"
                tvNama.text = "Nama"
                tvNomorHp.text = "Nomor Hp"
                tvAlamat.text = "Alamat"
                tvDetailAlamat.text = "Detail Alamat"
                tvJenisPlafon.text = "Jenis Plafon"
                tvJumlah.text = "Jumlah"
                tvHarga.text = "Harga"
                tvUkuran.text = "Ukuran"
                tvTotalHarga.text = "Total Harga"
                tvPembayaran.text = "Pembayaran"
                tvWaktu.text = "Waktu"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvIdPemesanan.setBackgroundResource(R.drawable.bg_table_title)
                tvNama.setBackgroundResource(R.drawable.bg_table_title)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table_title)
                tvAlamat.setBackgroundResource(R.drawable.bg_table_title)
                tvDetailAlamat.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisPlafon.setBackgroundResource(R.drawable.bg_table_title)
                tvJumlah.setBackgroundResource(R.drawable.bg_table_title)
                tvHarga.setBackgroundResource(R.drawable.bg_table_title)
                tvUkuran.setBackgroundResource(R.drawable.bg_table_title)
                tvTotalHarga.setBackgroundResource(R.drawable.bg_table_title)
                tvPembayaran.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktu.setBackgroundResource(R.drawable.bg_table_title)
                tvGambarPlafon.setBackgroundResource(R.drawable.bg_table_title)
//                ivGambarPlafon.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvIdPemesanan.setTextColor(Color.parseColor("#ffffff"))
                tvNama.setTextColor(Color.parseColor("#ffffff"))
                tvNomorHp.setTextColor(Color.parseColor("#ffffff"))
                tvAlamat.setTextColor(Color.parseColor("#ffffff"))
                tvDetailAlamat.setTextColor(Color.parseColor("#ffffff"))
                tvJenisPlafon.setTextColor(Color.parseColor("#ffffff"))
                tvJumlah.setTextColor(Color.parseColor("#ffffff"))
                tvHarga.setTextColor(Color.parseColor("#ffffff"))
                tvUkuran.setTextColor(Color.parseColor("#ffffff"))
                tvTotalHarga.setTextColor(Color.parseColor("#ffffff"))
                tvPembayaran.setTextColor(Color.parseColor("#ffffff"))
                tvWaktu.setTextColor(Color.parseColor("#ffffff"))
                tvGambarPlafon.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvIdPemesanan.setTypeface(null, Typeface.BOLD)
                tvAlamat.setTypeface(null, Typeface.BOLD)
                tvJenisPlafon.setTypeface(null, Typeface.BOLD)
                tvJumlah.setTypeface(null, Typeface.BOLD)
                tvHarga.setTypeface(null, Typeface.BOLD)
                tvUkuran.setTypeface(null, Typeface.BOLD)
                tvTotalHarga.setTypeface(null, Typeface.BOLD)
                tvPembayaran.setTypeface(null, Typeface.BOLD)
                tvWaktu.setTypeface(null, Typeface.BOLD)
                tvGambarPlafon.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pesanan = listPesanan[(position-1)]

                tvNo.text = "$position"
                tvIdPemesanan.text = pesanan.id_pemesanan
                tvNama.text = pesanan.nama_lengkap
                tvNomorHp.text = pesanan.nomor_hp
                tvAlamat.text = pesanan.alamat
                tvDetailAlamat.text = pesanan.detail_alamat
                tvJenisPlafon.text = pesanan.jenis_plafon
                tvJumlah.text = pesanan.jumlah
                tvHarga.text = rupiah.rupiah(pesanan.harga!!.trim().toLong())
                tvUkuran.text = pesanan.ukuran
                tvTotalHarga.text = rupiah.rupiah(pesanan.total_harga!!.trim().toLong())
                tvPembayaran.text = pesanan.metode_pembayaran
                tvWaktu.text = pesanan.waktu
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvIdPemesanan.setBackgroundResource(R.drawable.bg_table)
                tvNama.setBackgroundResource(R.drawable.bg_table)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table)
                tvAlamat.setBackgroundResource(R.drawable.bg_table)
                tvDetailAlamat.setBackgroundResource(R.drawable.bg_table)
                tvJenisPlafon.setBackgroundResource(R.drawable.bg_table)
                tvJumlah.setBackgroundResource(R.drawable.bg_table)
                tvHarga.setBackgroundResource(R.drawable.bg_table)
                tvUkuran.setBackgroundResource(R.drawable.bg_table)
                tvTotalHarga.setBackgroundResource(R.drawable.bg_table)
                tvPembayaran.setBackgroundResource(R.drawable.bg_table)
                tvWaktu.setBackgroundResource(R.drawable.bg_table)
//                ivGambarPlafon.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)
                tvGambarPlafon.visibility = View.GONE
                ivGambarPlafon.visibility = View.VISIBLE

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvIdPemesanan.setTextColor(Color.parseColor("#000000"))
                tvNama.setTextColor(Color.parseColor("#000000"))
                tvNomorHp.setTextColor(Color.parseColor("#000000"))
                tvAlamat.setTextColor(Color.parseColor("#000000"))
                tvDetailAlamat.setTextColor(Color.parseColor("#000000"))
                tvJenisPlafon.setTextColor(Color.parseColor("#000000"))
                tvJumlah.setTextColor(Color.parseColor("#000000"))
                tvHarga.setTextColor(Color.parseColor("#000000"))
                tvUkuran.setTextColor(Color.parseColor("#000000"))
                tvTotalHarga.setTextColor(Color.parseColor("#000000"))
                tvPembayaran.setTextColor(Color.parseColor("#000000"))
                tvWaktu.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvIdPemesanan.setTypeface(null, Typeface.NORMAL)
                tvNama.setTypeface(null, Typeface.NORMAL)
                tvNomorHp.setTypeface(null, Typeface.NORMAL)
                tvAlamat.setTypeface(null, Typeface.NORMAL)
                tvDetailAlamat.setTypeface(null, Typeface.NORMAL)
                tvJenisPlafon.setTypeface(null, Typeface.NORMAL)
                tvJumlah.setTypeface(null, Typeface.NORMAL)
                tvHarga.setTypeface(null, Typeface.NORMAL)
                tvUkuran.setTypeface(null, Typeface.NORMAL)
                tvTotalHarga.setTypeface(null, Typeface.NORMAL)
                tvPembayaran.setTypeface(null, Typeface.NORMAL)
                tvWaktu.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)


                Glide.with(holder.itemView)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${pesanan.gambar}")
                    .error(R.drawable.gambar_not_have_image)
                    .into(ivGambarPlafon)

                ivGambarPlafon.setOnClickListener {
                    onClick.clickGambarPesanan(pesanan.gambar!!, pesanan.jenis_plafon!!, it)
                }
                tvNama.setOnClickListener {
                    onClick.clickKeterangan("Nama Lengkap", pesanan.nama_lengkap!!, it)
                }
                tvNomorHp.setOnClickListener {
                    onClick.clickKeterangan("Nomor Hp", pesanan.nomor_hp!!, it)
                }
                tvAlamat.setOnClickListener {
                    onClick.clickKeterangan("Alamat", pesanan.alamat!!, it)
                }
                tvDetailAlamat.setOnClickListener {
                    onClick.clickKeterangan("Detail Alamat", pesanan.detail_alamat!!, it)
                }
                tvJenisPlafon.setOnClickListener {
                    onClick.clickJenisPlafon(pesanan.jenis_plafon!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(pesanan, it)
                }
            }
        }
    }
}