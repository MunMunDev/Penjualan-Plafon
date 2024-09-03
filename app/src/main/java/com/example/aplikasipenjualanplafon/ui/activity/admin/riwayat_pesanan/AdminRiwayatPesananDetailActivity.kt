package com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminPilihPlafonAdapter
import com.example.aplikasipenjualanplafon.adapter.AdminRiwayatPesananDetailAdapter
import com.example.aplikasipenjualanplafon.data.model.AdminPesananDetailModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminRiwayatPesananDetailBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogAdminRiwayatPesananDetailBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogAdminPilihPlafonBinding
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
class AdminRiwayatPesananDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRiwayatPesananDetailBinding
    private val viewModel: AdminRiwayatPesananDetailViewModel by viewModels()
    private lateinit var adapter: AdminRiwayatPesananDetailAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
//    private lateinit var idPemesanan: String
    private var rupiah = KonversiRupiah()
    private var tanggalDanWaktu = TanggalDanWaktu()
    private var checkKonfirmasi = "0"
    private var idUser = "0"
    private var waktu = "0"

    private var listPlafon: ArrayList<PlafonModel> = arrayListOf()
    private var idPlafon = "0"
    private var namaPlafon = ""
    private var namaUser = ""
    private var nomorHp = ""
    private var kecamatan = ""
    private var alamat = "0"
    private var detailAlamat = ""
    private var metodePembayaran = ""

    private var listIdPemesanan: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPesananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        fetchRiwayatPesanan(idUser)
        getRiwayatPesanan()
        fetchPlafon()
        getPlafon()
        getTambahRiwayatPesanan()
        getUpdatePesanan()
        getHapusPesanan()
    }


    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null){
            idUser = extras.getString("idUser")!!
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnTambah.setOnClickListener {
                setShowDialogTambah(idUser, namaUser, nomorHp, kecamatan, alamat, detailAlamat, metodePembayaran)
            }
        }
    }

    private fun setShowDialogTambah(idUser:String, namaUser:String, nomorHp:String, kecamatan:String, alamat:String, detailAlamat:String, metodePembayaran:String) {
        val view = AlertDialogAdminRiwayatPesananDetailBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            etNamaLengkap.setText(namaUser)
            etNomorHp.setText(nomorHp)
            etKecamatanKabKota.setText(kecamatan)
            etAlamat.setText(alamat)
            etDetailAlamat.setText(detailAlamat)

            tvPlafon.setOnClickListener {
                setPilihPlafon(view)
            }

            var numberPositionIdPemesanan = 0
            var selectedValueIdPemesanan = ""

            tvPlafon.setOnClickListener {
                setPilihPlafon(view)
            }

            Log.d("AdminRiwayatPesananActivityTAG", "size: ${listIdPemesanan.size}")

            for(value in listIdPemesanan){
                Log.d("AdminRiwayatPesananActivityTAG", "value: $value")
            }

            val arrayAdapterIdPemesanan = ArrayAdapter(
                this@AdminRiwayatPesananDetailActivity,
                android.R.layout.simple_spinner_item,
                listIdPemesanan
            )

            arrayAdapterIdPemesanan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spIdPemesanan.adapter = arrayAdapterIdPemesanan

            spIdPemesanan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPositionIdPemesanan = spIdPemesanan.selectedItemPosition
                    selectedValueIdPemesanan = spIdPemesanan.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spJenisPembayaran.adapter = arrayAdapterIdPemesanan


            // Jenis Pembayaran
            var numberPosition = 0
            var selectedValue = ""

            val arrayAdapter = ArrayAdapter.createFromResource(
                this@AdminRiwayatPesananDetailActivity,
                R.array.metode_pembayaran,
                android.R.layout.simple_spinner_item
            )
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spJenisPembayaran.adapter = arrayAdapter

            spJenisPembayaran.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spJenisPembayaran.selectedItemPosition
                    selectedValue = spJenisPembayaran.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spJenisPembayaran.adapter = arrayAdapter

            btnSimpan.setOnClickListener {
                var cek = false

                if(etNamaLengkap.text.toString().trim().isEmpty()){
                    etNamaLengkap.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etNomorHp.text.toString().trim().isEmpty()){
                    etNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etAlamat.text.toString().trim().isEmpty()){
                    etAlamat.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etDetailAlamat.text.toString().trim().isEmpty()){
                    etDetailAlamat.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etJumlah.text.toString().trim() == "0"){
                    Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
                    etJumlah.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etJumlah.text.toString().trim().isEmpty()){
                    etJumlah.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(tvPlafon.text.toString().trim().isEmpty()){
                    tvPlafon.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    val jumlah = etJumlah.text.toString().trim()
                    val valueNamaLengkap = etNamaLengkap.text.toString().trim()
                    val valueNomorHp = etNomorHp.text.toString().trim()
                    val valueKecamatan = etKecamatanKabKota.text.toString().trim()
                    val valueAlamat = etAlamat.text.toString().trim()
                    val valueDetailAlamat = etDetailAlamat.text.toString().trim()
                    val idPemesanan = selectedValueIdPemesanan

//                    Log.d("DetailTAG", "setShowDialogTambah: $idUser, $idPemesanan, $idPlafon, $valueNomorHp, $valueNamaLengkap, $valueAlamat, $valueDetailAlamat, $jumlah, $selectedValue")

                    postTambahRiwayatPesanan(idUser, idPemesanan, idPlafon, valueNamaLengkap, valueNomorHp, valueKecamatan, valueAlamat, valueDetailAlamat, jumlah, selectedValue)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

//    private fun setShowDialogTambah(idUser:String, idPemesanan:String, alamat:String, waktu:String) {
//        val view = AlertDialogAdminRiwayatPesananDetailBinding.inflate(layoutInflater)
//
//        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
//        alertDialog.setView(view.root)
//            .setCancelable(false)
//        val dialogInputan = alertDialog.create()
//        dialogInputan.show()
//
//        view.apply {
//
//            setSpinnerPlafon(view, "0")
//
//            btnSimpan.setOnClickListener {
//                var cek = false
//                if(etJumlah.text.toString().trim() == "0"){
//                    Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
//                    cek = true
//                }
//
//                if(!cek){
//                    var idPlafon = ""
//                    var jumlah = etJumlah.text.toString().trim()
//                    var metodePembayaran = etMetodePembayaran.text.toString().trim()
//
//
//                    postTambahPesanan(idUser, idPemesanan, idPlafon, alamat, jumlah, metodePembayaran, waktu)
//                    dialogInputan.dismiss()
//                }
//            }
//            btnBatal.setOnClickListener {
//                dialogInputan.dismiss()
//            }
//        }
//    }

//    private fun setSpinnerPlafon(view: AlertDialogAdminRiwayatPesananDetailBinding, cekIdPlafon: String) {
//        val arrayIdPlafon = ArrayList<String>()
//        val arrayPlafon = ArrayList<String>()
//
//        for(values in listPlafon){
//            arrayIdPlafon.add(values.id_plafon!!)
//            arrayPlafon.add(values.jenis_plafon!![0].jenis_plafon!!)
//        }
//
//        val arrayAdapter = ArrayAdapter(
//            this@AdminRiwayatPesananDetailActivity,
//            android.R.layout.simple_spinner_item,
//            arrayPlafon
//        )
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        view.apply {
//            spPlafon.adapter = arrayAdapter
//
//            if(cekIdPlafon != "0"){
//                for((cekNomor, value) in arrayIdPlafon.withIndex()){
//                    if(value.trim() == cekIdPlafon.trim()){
//                        spPlafon.setSelection(cekNomor)
//                    }
//                }
//            } else{
//                spPlafon.setSelection(0)
//            }
//
//            spPlafon.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val numberPosition = spPlafon.selectedItemPosition
//                    idPlafon = arrayIdPlafon[numberPosition]
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                }
//            }
//        }
//    }

    private fun setPilihPlafon(viewTambahPlafon: AlertDialogAdminRiwayatPesananDetailBinding) {
        val view = AlertDialogAdminPilihPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            rvPilihPlafon.layoutManager = LinearLayoutManager(
                this@AdminRiwayatPesananDetailActivity, LinearLayoutManager.VERTICAL, false
            )
            rvPilihPlafon.adapter = AdminPilihPlafonAdapter(listPlafon, object : OnClickItem.ClickPlafon{
                override fun clickItemDetail(plafon: PlafonModel, it: View) {

                }

                override fun clickItemPlafon(plafon: PlafonModel, it: View) {
                    idPlafon = plafon.id_plafon!!
                    namaPlafon = plafon.jenis_plafon!![0].jenis_plafon!!

                    viewTambahPlafon.tvPlafon.text = namaPlafon
                    dialogInputan.dismiss()
                }

                override fun clickItemKeterangan(plafon: PlafonModel, it: View) {

                }

                override fun clickItemImage(jenisPlafon: String, image: String) {
                    setShowImage(gambar = image, jenisPlafon = jenisPlafon)
                }
            })
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahRiwayatPesanan(
        idUser: String,
        idPemesanan: String,
        idPlafon: String,
        namaLengkap: String,
        nomorHp: String,
        kecamatan: String,
        alamat: String,
        detailAlamat: String,
        jumlah: String,
        metodePembayaran: String,
    ) {
        viewModel.postTambahRiwayatPesananDetail(idUser, idPemesanan, idPlafon, namaLengkap, nomorHp, kecamatan, alamat, detailAlamat, jumlah, metodePembayaran)
    }

    private fun getTambahRiwayatPesanan(){
        viewModel.getTambahRiwayatPesananDetail().observe(this@AdminRiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminRiwayatPesananDetailActivity)
                is UIState.Failure-> setFailureTambahRiwayatPesanan(result.message)
                is UIState.Success-> setSuccessTambahRiwayatPesanan(result.data)
            }
        }
    }

    private fun setFailureTambahRiwayatPesanan(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminRiwayatPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTambahRiwayatPesanan(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                fetchRiwayatPesanan(idUser)
            } else{
                Toast.makeText(this@AdminRiwayatPesananDetailActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Error di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchRiwayatPesanan(idUser: String) {
        viewModel.fetchPesanan(idUser)
    }
    private fun getRiwayatPesanan(){
        viewModel.getPesanan().observe(this@AdminRiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminRiwayatPesananDetailActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
//        Toast.makeText(this@AdminRiwayatPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
        Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        Log.d("AdminDetailPesananTAG", "data: $message")
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<AdminPesananDetailModel>) {
        loading.alertDialogCancel()
        listIdPemesanan = arrayListOf()
        if(data.isNotEmpty()){
            setAdapter(data[0].pesanan)
            setDataKeterangan(data)
            setFetchIdPemesanan(data[0].pesanan)
        } else{
            Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFetchIdPemesanan(pesanan: ArrayList<RiwayatPesananValModel>) {
        var idPemesanan = ""

        listIdPemesanan.add("Baru")
        for(value in pesanan){
            if(idPemesanan!=value.id_pemesanan){
                listIdPemesanan.add(value.id_pemesanan!!)
                idPemesanan = value.id_pemesanan

//                Log.d("AdminRiwayatPesananActivityTAG", "idPemesanan: $idPemesanan")
            }
        }

//        Log.d("AdminRiwayatPesananActivityTAG", "size: ${listIdPemesanan.size}")
//
//        for(value in listIdPemesanan){
//            Log.d("AdminRiwayatPesananActivityTAG", "value: $value")
//        }

    }

    private fun setDataKeterangan(data: ArrayList<AdminPesananDetailModel>){
        val valueData = data[0]
        binding.apply {
//            llKeterangan.visibility = View.VISIBLE

            tvNama.text = valueData.nama
            tvAlamat.text = valueData.alamat
            tvNomorHp.text = valueData.nomorHp
            val ket = if(valueData.ket=="0"){
                "Belum Dibayar"
            } else{
                "Sudah Dibayar"
            }
            checkKonfirmasi = valueData.ket!!
        }

//        idUser = valueData.id_user!!
        namaUser = valueData.nama!!
        nomorHp = valueData.nomorHp!!
        kecamatan = valueData.kecamatan!!
        alamat = valueData.alamat!!
        detailAlamat = valueData.detail_alamat!!
        waktu = valueData.waktu!!
        metodePembayaran = valueData.metodePembayaran!!
    }

    private fun setAdapter(data: ArrayList<RiwayatPesananValModel>) {
        adapter = AdminRiwayatPesananDetailAdapter(data, object: OnClickItem.ClickAdminRiwayatPesananDetail{
            override fun clickKeterangan(keterangan: String, isi:String, it: View) {
                showClickKeterangan(keterangan, isi)
            }
            override fun clickJenisPlafon(jenisPlafon: String, it: View) {
                showClickKeterangan("Jenis Plafon", jenisPlafon)
            }

            override fun clickGambarPesanan(gambar: String, jenisPlafon: String, it: View) {
                setShowImage(gambar, jenisPlafon)
            }

            override fun clickItemSetting(pesanan: RiwayatPesananValModel, it: View) {
                val popupMenu = PopupMenu(this@AdminRiwayatPesananDetailActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(pesanan)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(pesanan)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

        })

        binding.apply {
            rvDetailPesanan.layoutManager = LinearLayoutManager(
                this@AdminRiwayatPesananDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvDetailPesanan.adapter = adapter
        }

    }

    private fun showClickKeterangan(judul:String, keterangan: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
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

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
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

        Glide.with(this@AdminRiwayatPesananDetailActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setShowDialogHapus(pesanan: RiwayatPesananValModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus Riwayat Pesanan?"
            tvBodyKonfirmasi.text = "Pesanan yang anda pilih akan terhapus ${pesanan.jenis_plafon}"

            btnKonfirmasi.setOnClickListener {
                dialogInputan.dismiss()
                postHapusRiwayatPesanan(pesanan.id_riwayat_pesanan!!)
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postHapusRiwayatPesanan(idRiwayatPesanan: String) {
        Log.d("DetailTAG", "postHapusRiwayatPesanan: $idRiwayatPesanan")
        viewModel.postHapusRiwayatPesanan(idRiwayatPesanan)
    }

    private fun getHapusPesanan(){
        viewModel.getDeleteRiwayatPesananDetail().observe(this@AdminRiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminRiwayatPesananDetailActivity)
                is UIState.Success-> setSuccessHapusPesanan(result.data)
                is UIState.Failure-> setFailureHapusPesanan(result.message)
            }
        }
    }

    private fun setFailureHapusPesanan(message: String) {
        Toast.makeText(this@AdminRiwayatPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessHapusPesanan(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Berhasil hapus", Toast.LENGTH_SHORT).show()
                fetchRiwayatPesanan(idUser)
            } else{
                Toast.makeText(this@AdminRiwayatPesananDetailActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        }
        loading.alertDialogCancel()
    }

    private fun setShowDialogEdit(pesanan: RiwayatPesananValModel) {
        val view = AlertDialogAdminRiwayatPesananDetailBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etNamaLengkap.setText(pesanan.nama_lengkap)
            etNomorHp.setText(pesanan.nomor_hp)
            etKecamatanKabKota.setText(pesanan.kecamatan_kab_kota)
            etAlamat.setText(pesanan.alamat)
            etDetailAlamat.setText(pesanan.detail_alamat)
            etJumlah.setText(pesanan.jumlah)
            tvPlafon.text = pesanan.jenis_plafon
            idPlafon = pesanan.id_plafon!!

            tvPlafon.setOnClickListener {
                setPilihPlafon(view)
            }

            var numberPositionIdPemesanan = 0
            var selectedValueIdPemesanan = ""

            tvPlafon.setOnClickListener {
                setPilihPlafon(view)
            }

            Log.d("AdminRiwayatPesananActivityTAG", "size: ${listIdPemesanan.size}")

            for(value in listIdPemesanan){
                Log.d("AdminRiwayatPesananActivityTAG", "value: $value")
            }

            val arrayAdapterIdPemesanan = ArrayAdapter(
                this@AdminRiwayatPesananDetailActivity,
                android.R.layout.simple_spinner_item,
                listIdPemesanan
            )

            arrayAdapterIdPemesanan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spIdPemesanan.adapter = arrayAdapterIdPemesanan

            spIdPemesanan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPositionIdPemesanan = spIdPemesanan.selectedItemPosition
                    selectedValueIdPemesanan = spIdPemesanan.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spIdPemesanan.adapter = arrayAdapterIdPemesanan
            spIdPemesanan.setSelection(arrayAdapterIdPemesanan.getPosition(pesanan.id_pemesanan))


            // Jenis Pembayaran
            var numberPosition = 0
            var selectedValue = ""

            val arrayAdapter = ArrayAdapter.createFromResource(
                this@AdminRiwayatPesananDetailActivity,
                R.array.metode_pembayaran,
                android.R.layout.simple_spinner_item
            )
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spJenisPembayaran.adapter = arrayAdapter

            spJenisPembayaran.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spJenisPembayaran.selectedItemPosition
                    selectedValue = spJenisPembayaran.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spJenisPembayaran.adapter = arrayAdapter
            spJenisPembayaran.setSelection(arrayAdapter.getPosition(pesanan.metode_pembayaran))


            btnSimpan.setOnClickListener {
                var cek = false

                if(etAlamat.text.toString().trim().isEmpty()){
                    etAlamat.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etJumlah.text.toString().trim() == "0"){
                    Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
                    etJumlah.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etJumlah.text.toString().trim().isEmpty()){
                    etJumlah.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(tvPlafon.text.toString().trim().isEmpty()){
                    tvPlafon.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    val jumlah = etJumlah.text.toString().trim()
                    val valueNama = etNamaLengkap.text.toString().trim()
                    val valueNomorHp = etNomorHp.text.toString().trim()
                    val valueKecamatan = etKecamatanKabKota.text.toString().trim()
                    val valueAlamat = etAlamat.text.toString().trim()
                    val valueDetailAlamat = etDetailAlamat.text.toString().trim()
                    val idPemesanan = selectedValueIdPemesanan

                    postUpdateRiwayatPesanan(
                        pesanan.id_riwayat_pesanan!!, idUser, idPemesanan, idPlafon, valueNama, valueNomorHp, valueKecamatan, valueAlamat, valueDetailAlamat, jumlah, selectedValue
                    )
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateRiwayatPesanan(
        idRiwayatPesanan: String,
        idUser: String,
        idPemesanan: String,
        idPlafon: String,
        namaLengkap: String,
        nomorHp: String,
        kecamatan: String,
        alamat: String,
        detailAlamat: String,
        jumlah: String,
        metodePembayaran: String,
    ) {
        viewModel.postUpdateRiwayatPesanan(idRiwayatPesanan, idUser, idPemesanan, idPlafon, namaLengkap, nomorHp, kecamatan, alamat, detailAlamat, jumlah, metodePembayaran)
        Log.d("AdminRiwayatPesananDetailActivityTAG", "idRiwayatPesanan: $idRiwayatPesanan, idUser: $idUser, idPemesanan: $idPemesanan, idPlafon: $idPlafon, alamat: $alamat, jumlah: $jumlah, metodePembayaran: $metodePembayaran,  ")
    }

    private fun getUpdatePesanan(){
        viewModel.getUpdateRiwayatPesananDetail().observe(this@AdminRiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminRiwayatPesananDetailActivity)
                is UIState.Success-> setSuccessUpdatePesanan(result.data)
                is UIState.Failure-> setFailureUpdatePesanan(result.message)
            }
        }
    }

    private fun setFailureUpdatePesanan(message: String) {
        Toast.makeText(this@AdminRiwayatPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePesanan(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchRiwayatPesanan(idUser)
            } else{
                Toast.makeText(this@AdminRiwayatPesananDetailActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminRiwayatPesananDetailActivity, "Ada masalah di web", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun fetchPlafon(){
        viewModel.fetchPlafon()
    }

    private fun getPlafon(){
        viewModel.getPlafon().observe(this@AdminRiwayatPesananDetailActivity){result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Failure-> setFailureFetchPlafon(result.message)
                is UIState.Success-> setSuccessFetchPlafon(result.data)
            }
        }
    }

    private fun setFailureFetchPlafon(message: String) {

    }

    private fun setSuccessFetchPlafon(data: ArrayList<PlafonModel>) {
        listPlafon = data
    }
}