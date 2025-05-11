package com.example.aplikasipenjualanplafon.ui.activity.user.alamat

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.aplikasipenjualanplafon.data.model.KecamatanModel
import com.example.aplikasipenjualanplafon.data.model.ProvinsiModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPilihAlamatBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogPilihAlamatBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.pembayaran_online.PaymentActivity
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.ProvinsiIndonesia
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

    private lateinit var listProvinsi: ArrayList<ProvinsiModel>
    private lateinit var listKabKota: ArrayList<KabKotaModel>
    private lateinit var listKecamatan: ArrayList<KecamatanModel>
    private var listNamaProvinsi: ArrayList<String> = arrayListOf()
    private var listNamaKabKota: ArrayList<String> = arrayListOf()
    private var listNamaKecamatan: ArrayList<String> = arrayListOf()
    private var listIdProvinsi: ArrayList<String> = arrayListOf()
    private var listIdKabKota: ArrayList<String> = arrayListOf()
    private var listIdKecamatan: ArrayList<String> = arrayListOf()

//    private lateinit var valueIdProvinsi: String
//    private lateinit var valueIdKabKota: String
//    private lateinit var valueIdKecamatan: String

    private var tempSpKabKota: Spinner? = null
    private var tempSpKecamatan: Spinner? = null

    private var idKabKota = 0
    private var idKecamatan = 0

    val provinsiIndonesia = ProvinsiIndonesia()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setButton()
        fetchProvinsi()
        getProvinsi()
        getKabKota()
        getKecamatan()
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
//            val listKabKotaSulsel = kotaKab.kotaKabSulsel()

            tempSpKabKota = spKabKota
            tempSpKecamatan = spKecamatan

            setSpinnerProvinsi(spProvinsi, spKabKota, spKecamatan, listNamaProvinsi, listIdProvinsi, 0)

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
//                if(etAlamat.text.toString().trim().isEmpty()){
//                    etAlamat.error = "Tidak Boleh Kosong"
//                    cek = false
//                }
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
                        namaLengkap, nomorHp, idKecamatan.toString(), alamat, detailAlamat
                    )
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setSpinnerProvinsi(
        spProvinsi: Spinner, spKabKota: Spinner, spKecamatan: Spinner, valueListNamaProvinsi: ArrayList<String>,
        valueListIdProvinsi: ArrayList<String>, idProvinsi: Int
    ){
        // set kacamatan
//            val listProvinsi = kotaKab.kotaKabSulsel()
        var index = 0
        if(idProvinsi != 0){
            index = listProvinsi.indexOfFirst { it.id_provinsi==idProvinsi.toString() }

            if(index == -1){
                index = 0
            }
        }
        val arrayAdapterProvinsi = ArrayAdapter(
            this@PilihAlamatActivity,
            R.layout.simple_spinner_item,
            valueListNamaProvinsi
        )

        arrayAdapterProvinsi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvinsi.adapter = arrayAdapterProvinsi

        spProvinsi.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val valueIdProvinsi = valueListIdProvinsi[position].toInt()
                fetchKabKota(valueIdProvinsi)

                Log.d("DetailTAG", "idProvinsi: $valueIdProvinsi")
//                setSpinnerKabKota(spKabKota, spKecamatan, listNamaKabKota, listIdKabKota, 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spProvinsi.setSelection(index)
    }

    private fun setSpinnerKabKota(
        spKabKota: Spinner, spKecamatan: Spinner, valueListNamaKabKota: ArrayList<String>,
        valueListIdKabKota: ArrayList<String>, idKabKota: Int
    ){
        var index = 0
        if(idKabKota != 0){
            index = listKabKota.indexOfFirst { it.id_kab_kota==idKabKota.toString() }

            if(index == -1){
                index = 0
            }
        }

        val arrayAdapterKabKota = ArrayAdapter(
            this@PilihAlamatActivity,
            R.layout.simple_spinner_item,
            valueListNamaKabKota
        )

        arrayAdapterKabKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKabKota.adapter = arrayAdapterKabKota

        spKabKota.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val valueIdKabKota = valueListIdKabKota[position].toInt()
                fetchKecamatan(valueIdKabKota)
//                setSpinnerKecamatan(spKecamatan, listNamaKecamatan, listIdKecamatan, 0)

                Toast.makeText(this@PilihAlamatActivity, "$valueIdKabKota", Toast.LENGTH_SHORT).show()
                Log.d("DetailTAG", "idKabKota: $valueIdKabKota ")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spKabKota.setSelection(index)
    }

    private fun setSpinnerKecamatan(
        spKecamatan: Spinner, valueListNamaKecamatan: ArrayList<String>,
        valueListIdKecamatan: ArrayList<String>, valueIdKecamatan: Int
    ){
        var index = 0
        if(valueIdKecamatan != 0){
            index = listKecamatan.indexOfFirst { it.id_kecamatan==valueIdKecamatan.toString() }

            if(index == -1){
                index = 0
            }
        }

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
                idKecamatan = valueListIdKecamatan[position].trim().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spKecamatan.setSelection(index)
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
                    setShowDialogUpdateData(
                        data.id_alamat!!, data.nama_lengkap!!, data.nomor_hp!!,
                        data.alamat!!, data.detail_alamat!!, data.provinsi!!.id_provinsi!!,
                        data.provinsi.listKabKota!!.id_kab_kota!!, data.provinsi.listKabKota!!.listKecamatan!!.id_kecamatan!!
                    )
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
        nomorHp: String, alamat: String, detailAlamat: String,
        idProvinsi: String, valueIdKabKota:String, valueIdKecamatan: String
    ) {
        val view = AlertDialogPilihAlamatBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@PilihAlamatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        idKabKota = valueIdKabKota.trim().toInt()
        idKecamatan = valueIdKecamatan.trim().toInt()

        view.apply {
            etNamaLengkap.setText(namaLengkap)
            etNomorHp.setText(nomorHp)
            etAlamat.setText(alamat)
            etDetailAlamat.setText(detailAlamat)

            tempSpKabKota = spKabKota
            tempSpKecamatan = spKecamatan

            setSpinnerProvinsi(spProvinsi, spKabKota, spKecamatan, listNamaProvinsi, listIdProvinsi, idProvinsi.trim().toInt())

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
//                if(etAlamat.text.toString().trim().isEmpty()){
//                    etAlamat.error = "Tidak Boleh Kosong"
//                    cek = false
//                }
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
                        valueNamaLengkap, valueNomorHp, idKecamatan.toString(), valueAlamat, valueDetailAlamat
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

    private fun fetchProvinsi(){
        viewModel.fetchProvinsi()
    }

    private fun getProvinsi(){
        viewModel.getProvinsi().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> {}
                is UIState.Failure -> setFailureProvinsi(result.message)
                is UIState.Success -> setSuccessProvinsi(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureProvinsi(message: String) {
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessProvinsi(data: ArrayList<ProvinsiModel>) {
        if(data.isNotEmpty()){
            listProvinsi = arrayListOf()
            listNamaProvinsi = arrayListOf()
            listProvinsi = data
            for(value in data){
                listNamaProvinsi.add(value.provinsi!!)
                listIdProvinsi.add(value.id_provinsi!!.toString())
            }
        }
    }

    private fun fetchKabKota(idProvinsi: Int){
        viewModel.fetchKabKota(idProvinsi)
    }

    private fun getKabKota(){
        viewModel.getKabKota().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> {}
                is UIState.Failure -> setFailureKabKota(result.message)
                is UIState.Success -> setSuccessKabKota(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureKabKota(message: String) {
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessKabKota(data: ArrayList<KabKotaModel>) {
        if(data.isNotEmpty()){
            listKabKota = arrayListOf()
            listNamaKabKota = arrayListOf()
            listIdKabKota = arrayListOf()
            listKabKota = data
            for(value in data){
                listNamaKabKota.add(value.kab_kota!!)
                listIdKabKota.add(value.id_kab_kota!!)
            }

            setSpinnerKabKota(tempSpKabKota!!, tempSpKecamatan!!, listNamaKabKota, listIdKabKota, idKabKota)
        } else{
            Toast.makeText(this@PilihAlamatActivity, "Kota tidak ada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchKecamatan(idKabKota: Int){
        viewModel.fetchKecamatan(idKabKota)
    }

    private fun getKecamatan(){
        viewModel.getKecamatan().observe(this@PilihAlamatActivity){result->
            when(result){
                is UIState.Loading -> {}
                is UIState.Failure -> setFailureKecamatan(result.message)
                is UIState.Success -> setSuccessKecamatan(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureKecamatan(message: String) {
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessKecamatan(data: ArrayList<KecamatanModel>) {
        if(data.isNotEmpty()){
            listKecamatan = arrayListOf()
            listNamaKecamatan = arrayListOf()
            listIdKecamatan = arrayListOf()
            listKecamatan = data
            for(value in data){
                listNamaKecamatan.add(value.kecamatan!!)
                listIdKecamatan.add(value.id_kecamatan!!)
            }

            setSpinnerKecamatan(tempSpKecamatan!!, listNamaKecamatan, listIdKecamatan, idKecamatan)
        }
    }

    fun getLokalProvinsi(){
        listProvinsi = provinsiIndonesia.provinsi()
        listNamaProvinsi = arrayListOf()
        listIdProvinsi = arrayListOf()
        for(value in listProvinsi){
            listNamaProvinsi.add(value.provinsi!!)
            listIdProvinsi.add(value.id_provinsi!!)
        }
    }

    fun getLokalKabKota(idProvinsi: String){
        listKabKota = provinsiIndonesia.kabKota(idProvinsi)
        listNamaKabKota = arrayListOf()
        listIdKabKota = arrayListOf()
        for(value in listKabKota){
            listNamaKabKota.add(value.kab_kota!!)
            listIdKabKota.add(value.id_kab_kota!!)
        }
    }

    fun getLokalKecamatan(idKabKota: String){
        listKecamatan = provinsiIndonesia.kecamatan(idKabKota)
        listNamaKecamatan = arrayListOf()
        listIdKecamatan = arrayListOf()
        for(value in listKecamatan){
            listNamaKecamatan.add(value.kecamatan!!)
            listIdKecamatan.add(value.id_kecamatan!!)
        }
    }

}