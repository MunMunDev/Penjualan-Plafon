package com.example.aplikasipenjualanplafon.ui.activity.user.alamat

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.adapter.PilihAlamatAdapter
import com.example.aplikasipenjualanplafon.data.model.AlamatModel
import com.example.aplikasipenjualanplafon.data.model.KabKotaModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPilihAlamatBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogPilihAlamatBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.pembayaran_online.PaymentActivity
import com.example.aplikasipenjualanplafon.utils.KotaKabProvIndonesia
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PilihAlamatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPilihAlamatBinding
    private val viewModel: PilihAlamatViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferencesLogin
    @Inject lateinit var loading: LoadingAlertDialog
    @Inject lateinit var kotaKab: KotaKabProvIndonesia

    private lateinit var listKotaKab: ArrayList<KabKotaModel>
    private var listNamaKotaKab: ArrayList<String> = arrayListOf()
    private var listNamaKecamatan: ArrayList<String> = arrayListOf()
    private var listIdKecamatan: ArrayList<String> = arrayListOf()

    private lateinit var valueIdKecamatan: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setButton()
        fetchKabKota()
        getKabKota()
        fetchAlamat(sharedPreferences.getIdUser().toString())
        getAlamat()
        getUpdateMainAlamat()
        getTambahAlamat()
        getUpdateAlamat()
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                startActivity(Intent(this@PilihAlamatActivity, PaymentActivity::class.java))
                finish()
            }
            btnTambahAlamat.setOnClickListener {
                setShowDialogTambahData()
            }
        }
    }

    private fun setShowDialogTambahData() {
        val view = AlertDialogPilihAlamatBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@PilihAlamatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            // Set kab sulsel
//            val listKotaKabSulsel = kotaKab.kotaKabSulsel()
            val arrayAdapterKotaKab = ArrayAdapter(
                this@PilihAlamatActivity,
                R.layout.simple_spinner_item,
                listNamaKotaKab
            )

            arrayAdapterKotaKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKabKota.adapter = arrayAdapterKotaKab

            spKabKota.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val kabkota = spKabKota.selectedItem.toString()
                    var list = listKotaKab.filter {
                        it.kab_kota == kabkota
                    }

                    listNamaKecamatan = arrayListOf()
                    listIdKecamatan = arrayListOf()
                    for(value in list[0].listKecamatan!!){
                        listNamaKecamatan.add(value.kecamatan!!)
                        listIdKecamatan.add(value.id_kecamatan!!)
                    }

                    setSpinnerKecamatan(spKecamatan, listNamaKecamatan, listIdKecamatan, 0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            btnSimpan.setOnClickListener {
                var cek = true
                if(etNamaLengkap.text.toString().trim().isEmpty()){
                    etNamaLengkap.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etNomorHp.text.toString().trim().isEmpty()){
                    etNomorHp.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etAlamat.text.toString().trim().isEmpty()){
                    etAlamat.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etDetailAlamat.text.toString().trim().isEmpty()){
                    etDetailAlamat.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(cek){
                    val namaLengkap = etNamaLengkap.text.toString()
                    val nomorHp = etNomorHp.text.toString()
                    val alamat = etAlamat.text.toString()
                    val detailAlamat = etDetailAlamat.text.toString()

                    postTambahAlamat(
                        sharedPreferences.getIdUser().toString(),
                        namaLengkap, nomorHp, valueIdKecamatan, alamat, detailAlamat
                    )
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setSpinnerKecamatan(
        spKecamatan: Spinner, valueListNamaKecamatan: ArrayList<String>,
        valueListIdKecamatan: ArrayList<String>, idAlamat: Int){
        // set kacamatan
//            val listKecamatan = kotaKab.kotaKabSulsel()
        val arrayAdapterKecamatan = ArrayAdapter(
            this@PilihAlamatActivity,
            R.layout.simple_spinner_item,
            valueListNamaKecamatan
        )

        arrayAdapterKecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKecamatan.adapter = arrayAdapterKecamatan

        spKecamatan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                valueIdKecamatan = valueListIdKecamatan[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spKecamatan.setSelection(idAlamat)
    }

    private fun postTambahAlamat(
        idUser: String, namaLengkap: String, nomorHp: String,
        idKecamatan: String, alamat: String, detailAlamat: String
    ) {
        viewModel.postTambahAlamat(idUser, namaLengkap, nomorHp, idKecamatan, alamat, detailAlamat)
    }

    private fun getTambahAlamat(){
        viewModel.getTambahAlamat().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PilihAlamatActivity)
                is UIState.Failure -> setFailureTambahAlamat(result.message)
                is UIState.Success -> setSuccessTambahAlamat(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureTambahAlamat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTambahAlamat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@PilihAlamatActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                fetchAlamat(sharedPreferences.getIdUser().toString())
            } else{
                Toast.makeText(this@PilihAlamatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@PilihAlamatActivity)
    }

    private fun fetchKabKota(){
        viewModel.fetchKabKota()
    }

    private fun getKabKota(){
        viewModel.getKabKota().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Failure-> setFailureFetchKabKota(result.message)
                is UIState.Success-> setSuccessFetchKabKota(result.data)
            }
        }
    }

    private fun setFailureFetchKabKota(message: String) {

    }

    private fun setSuccessFetchKabKota(data: ArrayList<KabKotaModel>) {
        if(data.isNotEmpty()){
            listKotaKab = data
            for(value in data){
                listNamaKotaKab.add(value.kab_kota!!)
            }
        }
    }

    private fun fetchAlamat(idUser: String){
        viewModel.fetchDataAlamat(idUser)
    }
    private fun getAlamat(){
        viewModel.getDataAlamat().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> setStarShimmer()
                is UIState.Success -> setSuccessFetchAlamat(result.data)
                is UIState.Failure -> setFailureFetchAlamat(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureFetchAlamat(message: String) {
        setStopShimmer()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchAlamat(data: ArrayList<AlamatModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@PilihAlamatActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<AlamatModel>) {
        binding.apply {
            val adapter = PilihAlamatAdapter(data, object: OnClickItem.ClickPilihAlamat{
                override fun clickItemPilih(data: AlamatModel, it: View) {
                    postUpdateMainAlamat(data.id_alamat!!)
                }

                override fun clickItemEdit(data: AlamatModel, it: View) {
                    setShowDialogUpdateData(data.id_alamat!!, data.nama_lengkap!!, data.nomor_hp!!, data.alamat!!, data.detail_alamat!!)
                }

            })
            rvAlamat.layoutManager = LinearLayoutManager(this@PilihAlamatActivity, LinearLayoutManager.VERTICAL, false)
            rvAlamat.adapter = adapter
        }
    }

    private fun postUpdateMainAlamat(idAlamat: String) {
        viewModel.postUpdateMainAlamat(idAlamat, sharedPreferences.getIdUser().toString())
    }

    private fun getUpdateMainAlamat(){
        viewModel.getUpdateMainAlamat().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PilihAlamatActivity)
                is UIState.Failure -> setFailureUpdateMainAlamat(result.message)
                is UIState.Success -> setSuccessUpdateMainAlamat(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateMainAlamat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateMainAlamat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                startActivity(Intent(this@PilihAlamatActivity, PaymentActivity::class.java))
                finish()
            } else{
                Toast.makeText(this@PilihAlamatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setShowDialogUpdateData(
        idAlamat: String, namaLengkap: String,
        nomorHp: String, alamat: String, detailAlamat: String
    ) {
        val view = AlertDialogPilihAlamatBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@PilihAlamatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etNamaLengkap.setText(namaLengkap)
            etNomorHp.setText(nomorHp)
            etAlamat.setText(alamat)
            etDetailAlamat.setText(detailAlamat)

            var idSelection = 0

            val arrayAdapterKotaKab = ArrayAdapter(
                this@PilihAlamatActivity,
                R.layout.simple_spinner_item,
                listNamaKotaKab
            )

            arrayAdapterKotaKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKabKota.adapter = arrayAdapterKotaKab

            spKabKota.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val kabkota = spKabKota.selectedItem.toString()
                    var list = listKotaKab.filter {
                        it.kab_kota == kabkota
                    }

                    listNamaKecamatan = arrayListOf()
                    listIdKecamatan = arrayListOf()
                    for(value in list[0].listKecamatan!!){
                        listNamaKecamatan.add(value.kecamatan!!)
                        listIdKecamatan.add(value.id_kecamatan!!)
                    }

                    setSpinnerKecamatan(spKecamatan, listNamaKecamatan, listIdKecamatan, idSelection)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            btnSimpan.setOnClickListener {
                var cek = true
                if(etNamaLengkap.text.toString().trim().isEmpty()){
                    etNamaLengkap.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etNomorHp.text.toString().trim().isEmpty()){
                    etNomorHp.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etAlamat.text.toString().trim().isEmpty()){
                    etAlamat.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etDetailAlamat.text.toString().trim().isEmpty()){
                    etDetailAlamat.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(cek){
                    val valueNamaLengkap = etNamaLengkap.text.toString()
                    val valueNomorHp = etNomorHp.text.toString()
                    val valueAlamat = etAlamat.text.toString()
                    val valueDetailAlamat = etDetailAlamat.text.toString()

                    postUpdateAlamat(
                       idAlamat, sharedPreferences.getIdUser().toString(),
                        valueNamaLengkap, valueNomorHp, valueIdKecamatan, valueAlamat, valueDetailAlamat
                    )

                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateAlamat(
        idAlamat: String, idUser: String, namaLengkap: String,
        nomorHp: String, idKecamatan:String, alamat: String, detailAlamat: String
    ) {
        viewModel.postUpdateAlamat(idAlamat, idUser, namaLengkap, nomorHp, idKecamatan, alamat, detailAlamat)
    }

    private fun getUpdateAlamat(){
        viewModel.getUpdateAlamat().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PilihAlamatActivity)
                is UIState.Failure -> setFailureUpdateAlamat(result.message)
                is UIState.Success -> setSuccessUpdateAlamat(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateAlamat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateAlamat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@PilihAlamatActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchAlamat(sharedPreferences.getIdUser().toString())
            } else{
                Toast.makeText(this@PilihAlamatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setStarShimmer(){
        binding.apply {
            binding.apply {
                rvAlamat.visibility = View.GONE

                smAlamat.visibility = View.VISIBLE
                smAlamat.startShimmer()
            }
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            binding.apply {
                rvAlamat.visibility = View.VISIBLE

                smAlamat.visibility = View.GONE
                smAlamat.stopShimmer()
            }
        }
    }

}