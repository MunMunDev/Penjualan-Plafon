package com.example.aplikasipenjualanplafon.ui.activity.admin.keranjang_belanja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.KeranjangBelanjaDetailAdapter
import com.example.aplikasipenjualanplafon.adapter.RiwayatPesananDetailAdapter
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminKeranjangBelanjaDetailBinding
import com.example.aplikasipenjualanplafon.databinding.ActivityRiwayatPesananDetailBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan.RiwayatPesananDetailViewModel
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminKeranjangBelanjaDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminKeranjangBelanjaDetailBinding
    private val viewModel: AdminKeranjangBelanjaDetailViewModel by viewModels()
    private var idUser = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminKeranjangBelanjaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setDataSebelumnya()
        fetchRiwayatPembayaran()
        getRiwayatPembayaran()
    }

    private fun setButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            idUser = i.getString("idUser")!!
        }
    }

    private fun fetchRiwayatPembayaran() {
        viewModel.fetchDetailRiwayatPembayaran(idUser)
    }
    private fun getRiwayatPembayaran(){
        viewModel.getDetailRiwayatPembayaran().observe(this@AdminKeranjangBelanjaDetailActivity){ result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchRiwayatPembayaran(result.data)
                is UIState.Failure-> setFailureFetchRiwayatPembayaran(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayatPembayaran(message: String) {
        Toast.makeText(this@AdminKeranjangBelanjaDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchRiwayatPembayaran(data: ArrayList<RiwayatPesananValModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminKeranjangBelanjaDetailActivity, "Tidak ada data Jenis Plafon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: java.util.ArrayList<RiwayatPesananValModel>) {
        val adapter = KeranjangBelanjaDetailAdapter(data, object : OnClickItem.ClickRiwayatPesananDetail{
            override fun clickItemPesanan(pesanan: RiwayatPesananValModel, it: View) {

            }

            override fun clickGambarPesanan(gambar: String, jenisPlafon: String, it: View) {
                setShowImage(gambar, jenisPlafon)
            }

            override fun clicTestimoni(
                valueIdPemesanan: String,
                valueIdPlafon: String,
                valueJenisPlafon: String,
                it: View
            ) {
                Toast.makeText(this@AdminKeranjangBelanjaDetailActivity, "Maaf metode ini hanya bisa di riwayat pesanan", Toast.LENGTH_SHORT).show()
            }

        })
        binding.apply {
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@AdminKeranjangBelanjaDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, jenisPlafon: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminKeranjangBelanjaDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = jenisPlafon
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@AdminKeranjangBelanjaDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}