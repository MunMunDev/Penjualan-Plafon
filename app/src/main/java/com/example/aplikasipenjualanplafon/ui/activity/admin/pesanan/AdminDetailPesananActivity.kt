package com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminPesananDetailAdapter
import com.example.aplikasipenjualanplafon.data.model.AdminPesananDetailModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminDetailPesananBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKeteranganBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminDetailPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailPesananBinding
    private val viewModel: AdminDetailPesananViewModel by viewModels()
    private lateinit var adapter: AdminPesananDetailAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var idPemesanan: String
    private var rupiah = KonversiRupiah()
    private var tanggalDanWaktu = TanggalDanWaktu()
    private var checkKonfirmasi = "0"
    private var totalHarga = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        fetchData(idPemesanan)
        getPesanan()
        getUpdateKonfirmasiPembayaran()
        getUpdateKonfirmasiSelesai()
    }


    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null){
            idPemesanan = extras.getString("idPemesanan")!!
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnKonfirmasi.setOnClickListener {
                if(checkKonfirmasi == "0"){
                    showDialogCekKonfirmasi(
                        "Konfirmasi Pembayaran",
                        "User telah melakukan pembayaran sebesar $totalHarga \nDan data yang ada pada halaman ini akan di update"
                    )
                } else{
                    showDialogCekKonfirmasi(
                        "Konfirmasi Selesai",
                        "Anda telah melakukan pemasangan plafon ke user"
                    )
                }
            }
        }
    }

    private fun showDialogCekKonfirmasi(judul: String, isi:String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDetailPesananActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = judul
            tvBodyKonfirmasi.text = isi

            btnKonfirmasi.setOnClickListener {
                if(checkKonfirmasi == "0"){
                    postUpdateKonfirmasiPembayaran(idPemesanan)
                } else{
                    postUpdateKonfirmasiSelesai(idPemesanan)
                }

                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateKonfirmasiPembayaran(idPemesanan: String) {
        viewModel.postUpdateKonfirmasiPembayaran(idPemesanan)
    }

    private fun getUpdateKonfirmasiPembayaran(){
        viewModel.getUpdateKonfirmasiPembayaran().observe(this@AdminDetailPesananActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDetailPesananActivity)
                is UIState.Failure-> setFailureUpdateKonfirmasiPembayaran(result.message)
                is UIState.Success-> setSuccessUpdateKonfirmasiPembayaran(result.data)
            }
        }
    }

    private fun setFailureUpdateKonfirmasiPembayaran(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminDetailPesananActivity, "$message", Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateKonfirmasiPembayaran(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminDetailPesananActivity, "Berhasil Konfirmasi", Toast.LENGTH_SHORT).show()
                fetchData(idPemesanan)
            } else{
                Toast.makeText(this@AdminDetailPesananActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postUpdateKonfirmasiSelesai(idPemesanan: String) {
        viewModel.postUpdateKonfirmasiSelesai(idPemesanan)
    }

    private fun getUpdateKonfirmasiSelesai(){
        viewModel.getUpdateKonfirmasiSelesai().observe(this@AdminDetailPesananActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDetailPesananActivity)
                is UIState.Failure-> setFailureUpdateKonfirmasiSelesai(result.message)
                is UIState.Success-> setSuccessUpdateKonfirmasiSelesai(result.data)
            }
        }
    }

    private fun setFailureUpdateKonfirmasiSelesai(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminDetailPesananActivity, "$message", Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateKonfirmasiSelesai(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminDetailPesananActivity, "Berhasil Konfirmasi", Toast.LENGTH_SHORT).show()
                finish()
            } else{
                Toast.makeText(this@AdminDetailPesananActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchData(idPemesanan: String) {
        viewModel.fetchPesanan(idPemesanan)
    }
    private fun getPesanan(){
        viewModel.getPesanan().observe(this@AdminDetailPesananActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDetailPesananActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminDetailPesananActivity, message, Toast.LENGTH_SHORT).show()
        Log.d("AdminDetailPesananTAG", "data: $message")
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<AdminPesananDetailModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data[0].pesanan)
            setDataKeterangan(data)
        } else{
            Toast.makeText(this@AdminDetailPesananActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDataKeterangan(data: ArrayList<AdminPesananDetailModel>){
        val valueData = data[0]
        binding.apply {
            llKeterangan.visibility = View.VISIBLE

            tvNama.text = valueData.nama
            tvNomorHp.text = valueData.nomorHp
            tvKecamatan.text = "Kecamatan ${valueData.kecamatan}"
            tvAlamat.text = valueData.alamat
            tvDetailAlamat.text = valueData.detail_alamat

            var ket = ""
            if(valueData.ket=="0"){
                ket = "Belum Dibayar"
                btnKonfirmasi.text = "Konfirmasi Pembayaran"
            } else{
                ket = "Sudah Dibayar"
                btnKonfirmasi.text = "Konfirmasi Selesai"
            }
            checkKonfirmasi = valueData.ket!!
            totalHarga = rupiah.rupiah(valueData.totalHarga!!.toLong())
            tvPembayaran.text = valueData.metodePembayaran
            tvDibayar.text = ket
            tvTotalHarga.text = rupiah.rupiah(valueData.totalHarga!!.toLong())
            val arrayTanggalDanWaktu = valueData.waktu!!.split(" ")
            val tanggal = tanggalDanWaktu.konversiBulan(arrayTanggalDanWaktu[0])
            val waktu = tanggalDanWaktu.waktuNoSecond(arrayTanggalDanWaktu[1])
            tvWaktu.text = "$tanggal - $waktu"
        }
    }

    private fun setAdapter(data: ArrayList<RiwayatPesananValModel>) {
        adapter = AdminPesananDetailAdapter(data, object: OnClickItem.ClickAdminDetailPesanan{
            override fun clickItemSetting(pesanan: RiwayatPesananValModel, no: String, it: View){

            }

            override fun clickAlamatPesanan(alamat: String, it: View) {
                showClickKeterangan("Alamat", alamat)
            }

            override fun clickGambarPesanan(gambar: String, jenisPlafon: String, it: View) {
                setShowImage(gambar, jenisPlafon)
            }

        })

        binding.apply {
            rvDetailPesanan.layoutManager = LinearLayoutManager(
                this@AdminDetailPesananActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvDetailPesanan.adapter = adapter
        }

    }

    private fun showClickKeterangan(judul:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDetailPesananActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = judul
            tvBodyKeterangan.text = keterangan
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowImage(gambar: String, jenisPlafon: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDetailPesananActivity)
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

        Glide.with(this@AdminDetailPesananActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}