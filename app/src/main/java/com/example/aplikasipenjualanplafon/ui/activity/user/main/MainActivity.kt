package com.example.aplikasipenjualanplafon.ui.activity.user.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.aplikasipenjualanplafon.adapter.PesananAdapter
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityMainBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKonfirmasiPemesananBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogPesanPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogShowImageBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.akun.AkunActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.pembayaran_online.PaymentActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.plafon.PlafonActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan.RiwayatPesananActivity
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject lateinit var loading: LoadingAlertDialog
    @Inject lateinit var rupiah: KonversiRupiah
    private lateinit var pesananAdapter: PesananAdapter
    private var listPesanan: ArrayList<PesananModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavitagionDrawer()
        setSharedPreferencesLogin()
        setButton()
        fetchPesanan()
        getPesanan()
        getHapusPesanan()
        getUpdatePesanan()
        getPesan()
    }

    private fun setNavitagionDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@MainActivity)
    }

    private fun setButton() {
        binding.apply {
            btnPlafon.setOnClickListener {
                startActivity(Intent(this@MainActivity, PlafonActivity::class.java))
            }
            btnRiwayatPembayaran.setOnClickListener {
                startActivity(Intent(this@MainActivity, RiwayatPesananActivity::class.java))
            }
            btnAkun.setOnClickListener {
                startActivity(Intent(this@MainActivity, AkunActivity::class.java))
            }
            btnPesan.setOnClickListener {
//                showDialogPesan(listPesanan)
                val i = Intent(this@MainActivity, PaymentActivity::class.java)
                i.putParcelableArrayListExtra("pesanan", listPesanan)
                startActivity(i)
            }
        }
    }

    private fun showDialogPesan(listPesanan: ArrayList<PesananModel>){
        val view = AlertDialogKonfirmasiPemesananBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.etAlamatLengkap.setText(sharedPreferencesLogin.getAlamat())
        val totalHarga = rupiah.rupiah(searchTotalPrice(listPesanan))
        view.etHargaTotal.setText(totalHarga)

        var numberPosition = 0
        var selectedValue = ""

        view.apply {
//            etJenisPlafon.setText(plafonModel.jenis_plafon!![0].jenis_plafon)

//            val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, Resources.getSystem().getStringArray(R.array.jenis_pembayaran))
//            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val arrayAdapter = ArrayAdapter.createFromResource(
                this@MainActivity,
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
                if(etAlamatLengkap.text.toString().trim().isEmpty()){
                    etAlamatLengkap.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    val alamat = etAlamatLengkap.text.toString()
                    if(numberPosition==0){
                        val i = Intent(this@MainActivity, PaymentActivity::class.java)
                        i.putExtra("alamat", alamat)
                        i.putExtra("jenisPembayaran", selectedValue)
                        i.putParcelableArrayListExtra("pesanan", listPesanan)
                        startActivity(i)
                    } else if(numberPosition==1){
                        postPesan(listPesanan[0].idUser!!, alamat, selectedValue)

                        dialogInputan.dismiss()
                    }
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postPesan(idUser: String, alamat:String, metode_pembayaran: String) {
        viewModel.postPesan(idUser, alamat, metode_pembayaran)
    }

    private fun getPesan(){
        viewModel.getPostPesan().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@MainActivity)
                is UIState.Failure-> setFailurePostPesan(result.message)
                is UIState.Success-> setSuccessPostPesan(result.data)
            }
        }
    }

    private fun setSuccessPostPesan(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@MainActivity, "Berhasil pesan", Toast.LENGTH_SHORT).show()
                fetchPesanan()
            } else{
                Toast.makeText(this@MainActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@MainActivity, "Ada yang error di message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailurePostPesan(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun searchTotalPrice(data:ArrayList<PesananModel>): Long{
        var total: Long = 0
        for(value in data){
            val hargaTotalSatuan = value.jumlah!!.toInt()*value.plafon!![0].harga!!.toInt()
            total+=hargaTotalSatuan.toLong()
        }

        return total
    }

    private fun fetchPesanan() {
        viewModel.fetchPesanan(sharedPreferencesLogin.getIdUser().toString())
    }

    private fun getPesanan() {
        viewModel.getPesanan().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@MainActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setSuccessFetchPesanan(data: ArrayList<PesananModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
            setHaveData()

            listPesanan = data
        } else{
            Toast.makeText(this@MainActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
            setNoHaveData()
        }
        loading.alertDialogCancel()
    }

    private fun setAdapter(data: ArrayList<PesananModel>) {
        pesananAdapter = PesananAdapter(data, object : OnClickItem.ClickPesanan{
            override fun clickItemPesanan(pesanan: PesananModel, it: View) {
                val popupMenu = PopupMenu(this@MainActivity, it)
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

            override fun clickGambarPesanan(gambar: String, jenisPlafon: String, it: View) {
                setShowImage(gambar, jenisPlafon)
            }

        })
        binding.apply {
            rvPembayaran.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            rvPembayaran.adapter = pesananAdapter
        }

    }

    private fun setShowDialogHapus(pesanan: PesananModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus Pesanan?"
            tvBodyKonfirmasi.text = "Pesanan yang anda pilih akan terhapus"

            btnKonfirmasi.setOnClickListener {
                dialogInputan.dismiss()
                postHapusPesanan(pesanan.id_pesanan)
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postHapusPesanan(idPesanan: String?) {
        viewModel.postHapusPesanan(idPesanan!!)
    }

    private fun getHapusPesanan(){
        viewModel.getPostHapusPesanan().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@MainActivity)
                is UIState.Success-> setSuccessHapusPesanan(result.data)
                is UIState.Failure-> setFailureHapusPesanan(result.message)
            }
        }
    }

    private fun setFailureHapusPesanan(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessHapusPesanan(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@MainActivity, "Berhasil hapus", Toast.LENGTH_SHORT).show()
                fetchPesanan()
            } else{
                Toast.makeText(this@MainActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        }
        loading.alertDialogCancel()
    }

    private fun setShowDialogEdit(pesanan: PesananModel) {
        val view = AlertDialogPesanPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvNamaPlafon.text = pesanan.plafon!![0].jenis_plafon!![0].jenis_plafon
            tvStok.text = pesanan.plafon[0].stok
            tvHargaPlafon.text = rupiah.rupiah(pesanan.plafon[0].harga!!.toLong())
            tvUkuran.text = pesanan.plafon[0].ukuran
            tvJumlah.text = pesanan.jumlah

            btnTambah.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if(jumlah < pesanan.plafon[0].stok!!.toInt()){
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
                    Toast.makeText(this@MainActivity, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
                    cek = true
                }

                if(!cek){
                    dialogInputan.dismiss()
                    val idPesanan = pesanan.id_pesanan!!
                    val jumlah = tvJumlah.text.toString().trim()

                    postUpdatePesanan(idPesanan, jumlah)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdatePesanan(idPesanan: String, jumlah: String) {
        viewModel.postUpadatePesanan(idPesanan, jumlah)
    }

    private fun getUpdatePesanan(){
        viewModel.getPostUpdatePesanan().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@MainActivity)
                is UIState.Success-> setSuccessUpdatePesanan(result.data)
                is UIState.Failure-> setFailureUpdatePesanan(result.message)
            }
        }
    }

    private fun setFailureUpdatePesanan(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePesanan(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@MainActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchPesanan()
            } else{
                Toast.makeText(this@MainActivity, "${data[0].message_response}", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@MainActivity, "Ada masalah di web", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setShowImage(gambar: String, jenisPlafon: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@MainActivity)
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

        Glide.with(this@MainActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        setNoHaveData()
        loading.alertDialogCancel()
    }

    private fun setNoHaveData(){
        binding.apply {
            rvPembayaran.visibility = View.GONE
            btnPesan.visibility = View.GONE

            tvNotHavePesanan.visibility = View.VISIBLE
        }
    }

    private fun setHaveData(){
        binding.apply {
            rvPembayaran.visibility = View.VISIBLE
            btnPesan.visibility = View.VISIBLE

            tvNotHavePesanan.visibility = View.GONE
        }
    }
}