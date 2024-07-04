package com.example.aplikasipenjualanplafon.ui.activity.user.pesanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.RiwayatPesananDetailAdapter
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPesananDetailBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan.RiwayatPesananDetailViewModel
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PesananDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananDetailBinding
    private val viewModel: RiwayatPesananDetailViewModel by viewModels()
    private var idPemesanan = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananDetailBinding.inflate(layoutInflater)
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
            idPemesanan = i.getString("idPemesanan")!!
        }
    }

    private fun fetchRiwayatPembayaran() {
        viewModel.fetchDetailRiwayatPesanan(idPemesanan)
    }
    private fun getRiwayatPembayaran(){
        viewModel.getDetailRiwayatPesanan().observe(this@PesananDetailActivity){ result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchRiwayatPembayaran(result.data)
                is UIState.Failure-> setFailureFetchRiwayatPembayaran(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayatPembayaran(message: String) {
        Toast.makeText(this@PesananDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchRiwayatPembayaran(data: ArrayList<RiwayatPesananValModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
            if(data[0].ket == "0"){
                binding.tvKeterangan.text = "Belum Bayar"
            } else{
                binding.tvKeterangan.text = "Sudah Bayar"
            }
        } else{
            Toast.makeText(this@PesananDetailActivity, "Tidak ada data Jenis Plafon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: java.util.ArrayList<RiwayatPesananValModel>) {
        val adapter = RiwayatPesananDetailAdapter(data, object : OnClickItem.ClickRiwayatPesananDetail{
            override fun clickItemPesanan(pesanan: RiwayatPesananValModel, it: View) {

            }

            override fun clickGambarPesanan(gambar: String, jenisPlafon: String, it: View) {
                setShowImage(gambar, jenisPlafon)
            }

        })
        binding.apply {
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@PesananDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, jenisPlafon: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@PesananDetailActivity)
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

        Glide.with(this@PesananDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}