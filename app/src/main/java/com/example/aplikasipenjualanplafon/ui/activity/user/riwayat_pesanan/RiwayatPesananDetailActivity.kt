package com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.RiwayatPesananDetailAdapter
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.data.model.TestimoniModel
import com.example.aplikasipenjualanplafon.databinding.ActivityRiwayatPesananDetailBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogTestimoniTambahBinding
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RiwayatPesananDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPesananDetailBinding
    private val viewModel: RiwayatPesananDetailViewModel by viewModels()
    private var idPemesanan = "1"
    private var idPlafon = "1"
    private var jenisPlafon = ""
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPesananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setDataSebelumnya()
        getRiwayatPembayaran()
        getTestimoni()
        getTambahTestimoni()
        getUpdateTestimoni()
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

            fetchRiwayatPembayaran(idPemesanan)
        }
    }

    private fun fetchRiwayatPembayaran(idPemesanan: String) {
        viewModel.fetchDetailRiwayatPesanan(idPemesanan)
    }
    private fun getRiwayatPembayaran(){
        viewModel.getDetailRiwayatPesanan().observe(this@RiwayatPesananDetailActivity){ result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchRiwayatPembayaran(result.data)
                is UIState.Failure-> setFailureFetchRiwayatPembayaran(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayatPembayaran(message: String) {
        Toast.makeText(this@RiwayatPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchRiwayatPembayaran(data: ArrayList<RiwayatPesananValModel>) {
        if(data.isNotEmpty()){
            setAlamat(data)
            setAdapter(data)
        } else{
            Toast.makeText(this@RiwayatPesananDetailActivity, "Tidak ada data Jenis Plafon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAlamat(data: ArrayList<RiwayatPesananValModel>) {
        binding.apply {
            tvNama.text = data[0].nama_lengkap
            tvNomorHp.text = data[0].nomor_hp
            tvKecamatan.text = "Kecamatan ${data[0].kecamatan_kab_kota}"
            tvAlamat.text = data[0].alamat
            tvAlamatDetail.text = data[0].detail_alamat
        }
    }

    private fun setAdapter(data: java.util.ArrayList<RiwayatPesananValModel>) {
        val adapter = RiwayatPesananDetailAdapter(data, object : OnClickItem.ClickRiwayatPesananDetail{
            override fun clickItemPesanan(pesanan: RiwayatPesananValModel, it: View) {

            }

            override fun clickGambarPesanan(gambar: String, jenisPlafon: String, it: View) {
                setShowImage(gambar, jenisPlafon)
            }

            override fun clicTestimoni(valueIdPemesanan: String, valueIdPlafon: String, valueJenisPlafon: String, it: View) {
                idPlafon = valueIdPlafon
                jenisPlafon = valueJenisPlafon
                loading.alertDialogLoading(this@RiwayatPesananDetailActivity)
                fetchTestimoni(idPemesanan, idPlafon)
            }

        })
        binding.apply {
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@RiwayatPesananDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun fetchTestimoni(idPemesanan: String, idPlafon: String) {
        Log.d("DetailTAG", "fetchTestimoni: $idPemesanan, $idPlafon")
        viewModel.fetchTestimoni(idPemesanan, idPlafon)
    }

    private fun getTestimoni(){
        viewModel.getTestimoni().observe(this@RiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Failure-> setFailureFetchTestimoni(result.message)
                is UIState.Success-> setSuccessFetchTestimoni(result.data)
            }
        }
    }

    private fun setFailureFetchTestimoni(message: String) {

    }

    private fun setSuccessFetchTestimoni(data: ArrayList<TestimoniModel>) {
        if(data.isNotEmpty()){
            loading.alertDialogCancel()
//            setDataTestimoni(data)
            showTestimoni(data)
        } else{
            val listKosong = arrayListOf<TestimoniModel>()
            loading.alertDialogCancel()
            showTestimoni(listKosong)
        }
    }

    private fun showTestimoni(data: ArrayList<TestimoniModel>) {
        val view = AlertDialogTestimoniTambahBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@RiwayatPesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            //set data
            var idTestimoni = ""
            var jumlahBintang = 0
            tvJenisPlafon.text = jenisPlafon
            if(data.isNotEmpty()){
                jumlahBintang = data[0].bintang!!.trim().toInt()
                idTestimoni = data[0].id_testimoni!!
                val komentar = data[0].testimoni!!
                val bintang = data[0].bintang!!.trim().toInt()

                etTestimoni.setText(komentar)

                when (bintang) {
                    1 -> {
                        ivPostBintang1.setImageResource(R.drawable.icon_star_aktif)
                    }
                    2 -> {
                        ivPostBintang1.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang2.setImageResource(R.drawable.icon_star_aktif)
                    }
                    3 -> {
                        ivPostBintang1.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang2.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang3.setImageResource(R.drawable.icon_star_aktif)
                    }
                    4 -> {
                        ivPostBintang1.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang2.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang3.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang4.setImageResource(R.drawable.icon_star_aktif)
                    }
                    5 -> {
                        ivPostBintang1.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang2.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang3.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang4.setImageResource(R.drawable.icon_star_aktif)
                        ivPostBintang5.setImageResource(R.drawable.icon_star_aktif)
                    }
                }
            } else{
                idTestimoni = ""
            }

            ivPostBintang1.setOnClickListener {
                setBintang1(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 1
            }
            ivPostBintang2.setOnClickListener {
                setBintang2(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 2
            }
            ivPostBintang3.setOnClickListener {
                setBintang3(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 3
            }
            ivPostBintang4.setOnClickListener {
                setBintang4(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 4
            }
            ivPostBintang5.setOnClickListener {
                setBintang5(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 5
            }

            btnSimpan.setOnClickListener {
                if(jumlahBintang==0){
                    Toast.makeText(this@RiwayatPesananDetailActivity, "Masukkan Jumlah Bintang", Toast.LENGTH_SHORT).show()
                } else{
                    var check = true
                    if(etTestimoni.text.isEmpty()){
                        etTestimoni.error = "Masukkan Testimoni"
                        check = false
                    }

                    if(check){
                        dialogInputan.dismiss()
                        val testimoni = etTestimoni.text.toString()
                        val bintang = jumlahBintang.toString()

                        if(idTestimoni.isEmpty()){
                            postTambahTestimoni(
                                idPemesanan, idPlafon,testimoni, bintang
                            )
                        } else{
                            postUpdateTestimoni(idTestimoni, testimoni, bintang)
                        }
                    }

                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setBintang1(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_non_aktif)
        iv3.setImageResource(R.drawable.icon_star_non_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang2(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_non_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang3(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang4(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_aktif)
        iv4.setImageResource(R.drawable.icon_star_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang5(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_aktif)
        iv4.setImageResource(R.drawable.icon_star_aktif)
        iv5.setImageResource(R.drawable.icon_star_aktif)
    }

    private fun postTambahTestimoni(idPemesanan: String, idPlafon: String, testimoni: String, bintang: String) {
        viewModel.postTambahData(idPemesanan, idPlafon, testimoni, bintang)
    }

    private fun getTambahTestimoni(){
        viewModel.getResponseTambahTestimoni().observe(this@RiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@RiwayatPesananDetailActivity)
                is UIState.Failure-> setFailureTambahTestimoni(result.message)
                is UIState.Success-> setSuccessTambahTestimoni(result.data)
            }
        }
    }

    private fun setFailureTambahTestimoni(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@RiwayatPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTambahTestimoni(data: java.util.ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@RiwayatPesananDetailActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this@RiwayatPesananDetailActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postUpdateTestimoni(idTestimoni: String, testimoni: String, bintang: String){
        viewModel.postUpdatehData(idTestimoni, testimoni, bintang)
    }

    private fun getUpdateTestimoni(){
        viewModel.getResponseUpdateTestimoni().observe(this@RiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@RiwayatPesananDetailActivity)
                is UIState.Failure-> setFailureUpdateTestimoni(result.message)
                is UIState.Success-> setSuccessUpdateTestimoni(result.data)
            }
        }
    }

    private fun setFailureUpdateTestimoni(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@RiwayatPesananDetailActivity, "Gagal", Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateTestimoni(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@RiwayatPesananDetailActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this@RiwayatPesananDetailActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@RiwayatPesananDetailActivity, "", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShowImage(gambar: String, jenisPlafon: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@RiwayatPesananDetailActivity)
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

        Glide.with(this@RiwayatPesananDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}