package com.example.aplikasipenjualanplafon.ui.activity.user.plafon.detatil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.TestimoniAdapter
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.TestimoniModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPlafonDetailBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogPesanPlafonBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.plafon.PlafonActivity
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlafonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlafonDetailBinding
    private var idPlafon: String = ""
    private var jenisPlafon: String = ""
    private var keterangan: String = ""
    private var gambarPlafon: String = ""
    private var harga: String = ""
    private var rekomendasi: String = ""
    private var deskripsi: String = ""
    private var ukuran: String = ""
    private var stok: String = ""
    @Inject lateinit var rupiah : KonversiRupiah
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PlafonDetailViewModel by viewModels()
    private lateinit var adapter: TestimoniAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlafonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        fetchDataSebelumnya()
        getTestimoni()
        getTambahPesanan()
    }

    private fun fetchDataSebelumnya() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PlafonDetailActivity)
        val extras = intent.extras
        if(extras != null) {
            idPlafon = intent.getStringExtra("idPlafon")!!
            jenisPlafon = intent.getStringExtra("jenisPlafon")!!
            keterangan = intent.getStringExtra("keterangan")!!
            gambarPlafon = intent.getStringExtra("gambarPlafon")!!
            harga = intent.getStringExtra("harga")!!
            rekomendasi = intent.getStringExtra("rekomendasi")!!
            deskripsi = intent.getStringExtra("deskripsi")!!
            ukuran = intent.getStringExtra("ukuran")!!
            stok = intent.getStringExtra("stok")!!

            binding.apply {
                titleHeader.text = jenisPlafon
                tvHarga.text = rupiah.rupiah(harga.trim().toLong())
                tvRekomendasi.text = rekomendasi
                tvDeskripsi.text = keterangan

                Glide.with(this@PlafonDetailActivity)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${gambarPlafon}") // URL Gambar
                    .error(R.drawable.gambar_not_have_image)    // image ketika gagal
                    .into(ivGambarPlafon) // imageView mana yang akan diterapkan
            }

            fetchTestimoni(idPlafon)
        }
    }

    private fun fetchTestimoni(idPlafon: String){
        viewModel.fetchTestimoni(idPlafon)
    }

    private fun getTestimoni(){
        viewModel.getTestimoni().observe(this@PlafonDetailActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Failure-> setFailureFetchTestimoni(result.message)
                is UIState.Success-> setSuccessFetchTestimoni(result.data)
            }
        }
    }

    private fun setSuccessFetchTestimoni(data: ArrayList<TestimoniModel>) {
        if(data.isNotEmpty()){
            adapter = TestimoniAdapter(data, sharedPreferencesLogin.getIdUser().toString())
            binding.apply {
                rvTestimoni.layoutManager = LinearLayoutManager(this@PlafonDetailActivity, LinearLayoutManager.VERTICAL, false)
                rvTestimoni.adapter = adapter
            }
        }
    }

    private fun setFailureFetchTestimoni(message: String) {

    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                startActivity(Intent(this@PlafonDetailActivity, PlafonActivity::class.java))
                finish()
            }
            btnPesan.setOnClickListener {
                var arrayJenisPlafon = ArrayList<JenisPlafonModel>()
                var jenisPlafon = JenisPlafonModel("", "", "")
                arrayJenisPlafon.add(jenisPlafon)
                var plafon = PlafonModel(
                    idPlafon, arrayJenisPlafon, "", "", stok, "", harga, ""
                )
                dialogTambahPesanan(plafon)
            }
        }
    }

    private fun dialogTambahPesanan(plafonModel: PlafonModel) {
        val view = AlertDialogPesanPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@PlafonDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvNamaPlafon.text = jenisPlafon

            tvJumlah.text =  "0"

            tvStok.text =  "Stok : "+plafonModel.stok
            tvUkuran.text =  "Ukuran : "+plafonModel.ukuran

            val konversiRupiah = KonversiRupiah()
            tvHargaPlafon.text = "Harga : "+konversiRupiah.rupiah(plafonModel.harga!!.toLong())
//            tvJumlah.text =  plafonModel.jumlah.toString()

            btnTambah.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if(jumlah < plafonModel.stok!!.toInt()){
                    jumlah+=1
                    tvJumlah.text = jumlah.toString()
                }
            }
            btnKurang.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if (jumlah > 0){
                    jumlah-=1
                    tvJumlah.text = jumlah.toString()
                }
            }

            btnSimpan.setOnClickListener {
                var cek = false

                if(tvJumlah.text.toString().trim() == "0"){
                    cek = true
                }

                if(!cek){
                    val idPlafon = plafonModel.id_plafon!!
                    val idUser = sharedPreferencesLogin.getIdUser().toString()
                    val jumlah = tvJumlah.text.toString().trim()

                    postTambahPesanan(idPlafon, idUser, jumlah)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postTambahPesanan(
        idPlafon:String, idUser:String, jumlah: String
    ) {
        viewModel.postTambahPesanan(
            idPlafon, idUser, jumlah
        )
    }

    private fun getTambahPesanan() {
        viewModel.getTambahPesanan().observe(this@PlafonDetailActivity){ result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PlafonDetailActivity)
                is UIState.Failure-> setFailurePostTambahData(result.message)
                is UIState.Success-> setSuccessPostTambahData(result.data)
            }
        }
    }

    private fun setFailurePostTambahData(message: String) {
        Toast.makeText(this@PlafonDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessPostTambahData(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@PlafonDetailActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this@PlafonDetailActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@PlafonDetailActivity, "Ada kesahalan", Toast.LENGTH_SHORT).show()
        }

        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@PlafonDetailActivity, PlafonActivity::class.java))
        finish()
    }
}