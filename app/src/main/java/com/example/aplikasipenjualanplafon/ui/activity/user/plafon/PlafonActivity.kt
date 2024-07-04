package com.example.aplikasipenjualanplafon.ui.activity.user.plafon

import android.annotation.SuppressLint
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
import com.example.aplikasipenjualanplafon.adapter.PlafonAdapter
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogPesanPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlafonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlafonBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PlafonViewModel by viewModels()
    private lateinit var plafonAdapter: PlafonAdapter
    @Inject lateinit var loading: LoadingAlertDialog
    private var tempAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlafonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavitagionDrawer()
        setSharedPreferencesLogin()
        setButton()
        fetchPlafon()
        getPlafon()
        getTambahPlafon()
    }

    private fun setButton() {
        binding.apply {

        }
    }


    private fun setNavitagionDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@PlafonActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@PlafonActivity)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PlafonActivity)
    }

    private fun fetchPlafon() {
        viewModel.fetchPlafon()
    }

    private fun getPlafon() {
        viewModel.getPlafon().observe(this@PlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PlafonActivity)
                is UIState.Failure-> setFailureFetchPlafon(result.message)
                is UIState.Success-> setSuccessFetchPlafon(result.data)
            }
        }
    }

    private fun setFailureFetchPlafon(message: String) {
        Toast.makeText(this@PlafonActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPlafon(data: ArrayList<PlafonModel>) {
        if(data.isNotEmpty()){
            setListJenisPlafon(data)
            setAdapterPlafon(data)
        } else{
            Toast.makeText(this@PlafonActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
        setStopShimmer()
    }

    private fun setAdapterPlafon(data: ArrayList<PlafonModel>){
        plafonAdapter = PlafonAdapter(data, object : OnClickItem.ClickPlafon{
            override fun clickItemPlafon(plafon: PlafonModel, it: View) {
                Log.d("PlafonActivityTAG", "clickItemPlafon: ${plafon.harga}, ${plafon.jenis_plafon!![0].jenis_plafon}")

                dialogTambahPesanan(plafon)
            }

            override fun clickItemImage(jenisPlafon: String, image: String) {
                setShowImage(jenisPlafon, image)
            }

        })
        binding.apply {
            rvPlafon.layoutManager = LinearLayoutManager(this@PlafonActivity, LinearLayoutManager.VERTICAL, false)
            rvPlafon.adapter = plafonAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dialogTambahPesanan(plafonModel: PlafonModel) {
        val view = AlertDialogPesanPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@PlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvNamaPlafon.text = plafonModel.jenis_plafon!![0].jenis_plafon

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
                    tempAlertDialog = dialogInputan

                    val idPlafon = plafonModel.id_plafon!!
                    val idUser = sharedPreferencesLogin.getIdUser().toString()
                    val jumlah = tvJumlah.text.toString().trim()

                    postTambahPesanan(idPlafon, idUser, jumlah)
                }
            }
            btnBatal.setOnClickListener {
                tempAlertDialog = null
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

    private fun getTambahPlafon() {
        viewModel.getTambahPesanan().observe(this@PlafonActivity){ result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PlafonActivity)
                is UIState.Failure-> setFailurePostTambahData(result.message)
                is UIState.Success-> setSuccessPostTambahData(result.data)
            }
        }
    }

    private fun setFailurePostTambahData(message: String) {
        Toast.makeText(this@PlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessPostTambahData(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@PlafonActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                if(tempAlertDialog!=null){
                    tempAlertDialog!!.dismiss()
                    tempAlertDialog = null
                }
                fetchPlafon()
            } else{
                Toast.makeText(this@PlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@PlafonActivity, "Ada kesahalan", Toast.LENGTH_SHORT).show()
        }

        loading.alertDialogCancel()
    }

    private fun setShowImage(jenisPlafon: String, image: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@PlafonActivity)
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

        Glide.with(this@PlafonActivity)
            .load(image) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setListJenisPlafon(data: ArrayList<PlafonModel>){
        val jenisPlafonModel = arrayListOf<JenisPlafonModel>()
        jenisPlafonModel.add(JenisPlafonModel(
            id_jenis_plafon = "0",
            jenis_plafon = "Semua",
            keunggulan = ""
        ))

        val sort = data.sortedWith(compareBy { it.id_plafon })
        val dataArrayList = arrayListOf<PlafonModel>()
        dataArrayList.add(0,
            PlafonModel(
                id_plafon = "0",
                jenis_plafon = jenisPlafonModel,
                gambar = "",
                harga = ""
            )
        )
        dataArrayList.addAll(sort)


    }

    private fun setStopShimmer(){
        binding.apply {
            smPlafon.stopShimmer()
            smPlafon.visibility = View.GONE

            rvPlafon.visibility = View.VISIBLE
        }
    }
}