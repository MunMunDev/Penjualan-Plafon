package com.example.aplikasipenjualanplafon.ui.activity.admin.plafon

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminPlafonAdapter
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AdminPlafonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPlafonBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminPlafonViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    @Inject lateinit var rupiah: KonversiRupiah
    private lateinit var adapter: AdminPlafonAdapter
    private var listJenisPlafon = ArrayList<JenisPlafonModel>()

    private var tempView: AlertDialogPlafonBinding? = null
    private var tempDialog: AlertDialog? = null
    private var fileImage: MultipartBody.Part? = null

    private var idJenisPlafon: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPlafonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchJenisPlafon()
        getJenisPlafon()
        fetchDataPlafon()
        getDataPlafon()
        getAddPlafon()
        getUpdatePlafon()
        getUpdatePlafonNoImage()
        getDeletePlafon()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPlafonActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPlafonActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                showDialogTambahData()
            }
        }
    }

    private fun showDialogTambahData() {
        val view = AlertDialogPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        tempDialog = dialogInputan

        setSpinnerJenisPlafon(view, "0")

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            btnSimpan.setOnClickListener {
                var cek = false
                if(etHarga.text.toString().trim().isEmpty()){
                    etHarga.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etGambar.text.toString().trim() == resources.getString(R.string.add_image).trim() ){
                    etGambar.error = "Gambar tidak boleh kosong"
                    cek = true
                }

                if(!cek){
                    postAddPlafon(idJenisPlafon!!, fileImage!!, etHarga.text.toString().trim())
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempView = null
                tempDialog = null
                fileImage = null
                idJenisPlafon = null
            }
        }
    }

    private fun postAddPlafon(idJenisPlafon: String, image: MultipartBody.Part, harga: String) {
        viewModel.postTambahPlafon(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idJenisPlafon),
            fileImage!!,
            convertStringToMultipartBody(harga)
        )
    }

    private fun getAddPlafon(){
        viewModel.getTambahPlafon().observe(this@AdminPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPlafonActivity)
                is UIState.Success-> setSuccessAddPlafon(result.data)
                is UIState.Failure-> setFailureAddPlafon(result.message)
            }
        }
    }

    private fun setFailureAddPlafon(message: String) {
        Toast.makeText(this@AdminPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessAddPlafon(data: java.util.ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminPlafonActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                fetchDataPlafon()
                fileImage = null
                tempView = null
                tempDialog = null
                idJenisPlafon = null
            } else{
                Toast.makeText(this@AdminPlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPlafonActivity, "Erro di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchJenisPlafon() {
        viewModel.fetchJenisPlafon()
    }
    private fun getJenisPlafon(){
        viewModel.getJenisPlafon().observe(this@AdminPlafonActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchJenisPlafon(result.data)
                is UIState.Failure-> setFailureFetchJenisPlafon(result.message)
            }
        }
    }

    private fun setFailureFetchJenisPlafon(message: String) {
        Toast.makeText(this@AdminPlafonActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisPlafon(data: ArrayList<JenisPlafonModel>) {
        if(data.isNotEmpty()){
            listJenisPlafon = data
        } else{
            Toast.makeText(this@AdminPlafonActivity, "Tidak ada data Jenis Plafon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDataPlafon() {
        viewModel.fetchPlafon()
    }
    private fun getDataPlafon(){
        viewModel.getPlafon().observe(this@AdminPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPlafonActivity)
                is UIState.Failure-> setFailureFetchPlafon(result.message)
                is UIState.Success-> setSuccessFetchPlafon(result.data)
            }
        }
    }

    private fun setFailureFetchPlafon(message: String) {
        Toast.makeText(this@AdminPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
        setStopShimmer()
    }

    private fun setSuccessFetchPlafon(data: ArrayList<PlafonModel>) {
        loading.alertDialogCancel()
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        }
    }

    private fun setAdapter(data: ArrayList<PlafonModel>) {
        adapter = AdminPlafonAdapter(data, object : OnClickItem.ClickPlafon{
            override fun clickItemDetail(plafon: PlafonModel, it: View) {

            }

            override fun clickItemPlafon(plafon: PlafonModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPlafonActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(plafon)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(plafon)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickItemImage(jenisPlafon: String, image: String) {
                setShowImage(image, jenisPlafon)
            }

        })

        binding.apply {
            rvPlafon.layoutManager = LinearLayoutManager(this@AdminPlafonActivity, LinearLayoutManager.VERTICAL, false)
            rvPlafon.adapter = adapter
        }
    }

    private fun setShowDialogEdit(plafon: PlafonModel) {
        val view = AlertDialogPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        tempDialog = dialogInputan

        setSpinnerJenisPlafon(view, plafon.jenis_plafon!![0].id_jenis_plafon!!)

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            var idPlafon = plafon.id_plafon!!
            var harga = plafon.harga!!

            etHarga.setText(plafon.harga)

            btnSimpan.setOnClickListener {
                var cek = false
                if(etHarga.text.toString().trim().isEmpty()){
                    etHarga.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    if(etGambar.text.toString().trim() != resources.getString(R.string.ket_klik_file)){
                        postUpdatePlafon(idPlafon, idJenisPlafon!!, fileImage!!, harga)
                    } else{
                        postUpdatePlafonNoImage(idPlafon, idJenisPlafon!!, harga)
                    }
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                fileImage = null
                tempView = null
                tempDialog = null
                idJenisPlafon = null
            }
        }
    }

    private fun setSpinnerJenisPlafon(view: AlertDialogPlafonBinding, cekIdJenisPlafon: String) {
        val arrayIdJenisPlafon = ArrayList<String>()
        val arrayJenisPlafon = ArrayList<String>()

        for(values in listJenisPlafon){
            arrayIdJenisPlafon.add(values.id_jenis_plafon!!)
            arrayJenisPlafon.add(values.jenis_plafon!!)
        }

        val arrayAdapter = ArrayAdapter(
            this@AdminPlafonActivity,
            android.R.layout.simple_spinner_item,
            arrayJenisPlafon
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.apply {
            spJenisPlafon.adapter = arrayAdapter

            if(cekIdJenisPlafon != "0"){
                for((cekNomor, value) in arrayIdJenisPlafon.withIndex()){
                    if(value.trim() == cekIdJenisPlafon.trim()){
                        spJenisPlafon.setSelection(cekNomor)
                    }
                }
            } else{
                spJenisPlafon.setSelection(0)
            }

            spJenisPlafon.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val numberPosition = spJenisPlafon.selectedItemPosition
                    idJenisPlafon = arrayIdJenisPlafon[numberPosition]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun postUpdatePlafon(idPlafon: String, idJenisPlafon: String, gambarPlafon: MultipartBody.Part, harga: String) {
        viewModel.postUpdatePlafon(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idPlafon),
            convertStringToMultipartBody(idJenisPlafon),
            gambarPlafon,
            convertStringToMultipartBody(harga)
        )
    }
    private fun getUpdatePlafon(){
        viewModel.getUpdatePlafon().observe(this@AdminPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPlafonActivity)
                is UIState.Success-> setSuccessUpdatePlafon(result.data)
                is UIState.Failure-> setFailureUpdatePlafon(result.message)
            }
        }
    }

    private fun setFailureUpdatePlafon(message: String) {
        Toast.makeText(this@AdminPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePlafon(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminPlafonActivity, "Berhasil Update Plafon", Toast.LENGTH_SHORT).show()
                tempDialog!!.dismiss()
                fileImage = null
                tempView = null
                tempDialog = null
                idJenisPlafon = null
                fetchDataPlafon()
            } else{
                Toast.makeText(this@AdminPlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPlafonActivity, "Tidak ada respon dari web", Toast.LENGTH_SHORT).show()
        }
    }


    private fun postUpdatePlafonNoImage(idPlafon: String, idJenisPlafon: String, harga: String) {
        viewModel.postUpdatePlafonNoImage(
            "", idPlafon, idJenisPlafon, harga
        )
    }

    private fun getUpdatePlafonNoImage(){
        viewModel.getUpdatePlafonNoImage().observe(this@AdminPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPlafonActivity)
                is UIState.Failure-> setFailureUpdatePlafon(result.message)
                is UIState.Success-> setSuccessUpdatePlafon(result.data)
            }
        }
    }

    private fun setShowDialogHapus(plafon: PlafonModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Plafon?"
            tvBodyKonfirmasi.text = "Plafon yang dihapus tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                postDeletePlafon(plafon.id_plafon!!)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeletePlafon(idPlafon: String) {
        viewModel.postDeletePlafon(idPlafon)
    }

    private fun getDeletePlafon(){
        viewModel.getDeletePlafon().observe(this@AdminPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPlafonActivity)
                is UIState.Success-> setSuccessDeletePlafon(result.data)
                is UIState.Failure-> setFailureDeletePlafon(result.message)
            }
        }
    }

    private fun setFailureDeletePlafon(message: String) {
        Toast.makeText(this@AdminPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDeletePlafon(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminPlafonActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                tempDialog!!.dismiss()
                fetchDataPlafon()
            } else{
                Toast.makeText(this@AdminPlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminPlafonActivity, "Tidak ada respon dari web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShowImage(gambar: String, jenisPlafon: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPlafonActivity)
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

        Glide.with(this@AdminPlafonActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setStopShimmer(){
        binding.apply {
            smPlafon.stopShimmer()
            smPlafon.visibility = View.GONE

            rvPlafon.visibility = View.VISIBLE
        }
    }

    //Permission
    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(pdfUri: Uri?, pdfFileName: String, nameAPI:String): MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        pdfUri?.let {
            val file = contentResolver.openInputStream(pdfUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", pdfFileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, pdfFileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, pdfFileName: String, nameAPI:String): MultipartBody.Part?{
        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, pdfFileName, requestFile)

        return filePart
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminPlafonActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, Constant.STORAGE_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.STORAGE_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etGambar.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "gambar")
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminPlafonActivity, AdminMainActivity::class.java))
        finish()
    }

}