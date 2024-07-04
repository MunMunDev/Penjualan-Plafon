package com.example.aplikasipenjualanplafon.ui.activity.admin.semua_akun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminSemuaAkunAdapter
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.UsersModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminSemuaAkunBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogAkunBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKeteranganBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminSemuaAkunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSemuaAkunBinding
    private val viewModel: AdminSemuaAkunViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminSemuaAkunAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSemuaAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchSemuaAkun()
        getSemuaAkun()
        getTambahUser()
        getUpdateUser()
        getHapusUser()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminSemuaAkunActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminSemuaAkunActivity)
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
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            btnSimpan.setOnClickListener {
                val nama = etEditNama.text.toString().trim()
                val alamat = etEditAlamat.text.toString().trim()
                val nomorHp = etEditNomorHp.text.toString().trim()
                val username = etEditUsername.text.toString().trim()
                val password = etEditPassword.text.toString().trim()
                postTambahUser(nama, alamat, nomorHp, username, password)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahUser(
        nama: String, alamat: String, nomorHp: String,
        username: String, password: String
    ) {
        viewModel.postTambahAkun(nama, alamat, nomorHp, username, password, "user")
    }

    private fun getTambahUser(){
        viewModel.getTambahAkun().observe(this@AdminSemuaAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminSemuaAkunActivity)
                is UIState.Failure-> setFailureTambahAkun(result.message)
                is UIState.Success-> setSuccessTambahAkun(result.data)
            }
        }
    }

    private fun setSuccessTambahAkun(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this@AdminSemuaAkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
            fetchSemuaAkun()
        } else{
            Toast.makeText(this@AdminSemuaAkunActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureTambahAkun(message: String) {
        Toast.makeText(this@AdminSemuaAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchSemuaAkun() {
        viewModel.fetchAkun()
    }

    private fun getSemuaAkun(){
        viewModel.getAkun().observe(this@AdminSemuaAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminSemuaAkunActivity)
                is UIState.Failure-> setFailureFetchAkun(result.message)
                is UIState.Success-> setSuccessFetchAkun(result.data)
            }
        }
    }

    private fun setFailureFetchAkun(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminSemuaAkunActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchAkun(data: ArrayList<UsersModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminSemuaAkunActivity, "tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<UsersModel>) {
        binding.apply {
            adapter = AdminSemuaAkunAdapter(data, object: OnClickItem.ClickAkun{
                override fun clickItemSetting(akun: UsersModel, it: View) {
                    val popupMenu = PopupMenu(this@AdminSemuaAkunActivity, it)
                    popupMenu.inflate(R.menu.popup_edit_hapus)
                    popupMenu.setOnMenuItemClickListener(object :
                        PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                            when (menuItem!!.itemId) {
                                R.id.edit -> {
                                    setShowDialogEdit(akun)
                                    return true
                                }
                                R.id.hapus -> {
                                    setShowDialogHapus(akun)
                                    return true
                                }
                            }
                            return true
                        }

                    })
                    popupMenu.show()
                }

                override fun clickItemAlamat(alamat: String, it: View) {
                    showClickAlamat(alamat)
                }

            })
        }

        binding.apply {
            rvAkun.layoutManager = LinearLayoutManager(this@AdminSemuaAkunActivity, LinearLayoutManager.VERTICAL, false)
            rvAkun.adapter = adapter
        }

    }

    private fun showClickAlamat(alamat: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Alamat"
            tvBodyKeterangan.text = alamat
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(akun: UsersModel) {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditNama.setText(akun.nama)
            etEditAlamat.setText(akun.alamat)
            etEditNomorHp.setText(akun.nomorHp)
            etEditUsername.setText(akun.username)
            etEditPassword.setText(akun.password)

            btnSimpan.setOnClickListener {
                val idUser = akun.idUser!!
                val nama = etEditNama.text.toString().trim()
                val alamat = etEditAlamat.text.toString().trim()
                val nomorHp = etEditNomorHp.text.toString().trim()
                val username = etEditUsername.text.toString().trim()
                val password = etEditPassword.text.toString().trim()
                val usernameLama = akun.username!!
                postUpdateUser(idUser, nama, alamat, nomorHp, username, password, usernameLama)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateUser(
        idUser: String, nama: String, alamat: String, nomorHp: String,
        username: String, password: String, usernameLama: String
    ) {
        viewModel.postUpdateAkun(idUser, nama, alamat, nomorHp, username, password, usernameLama)
    }

    private fun getUpdateUser(){
        viewModel.getUpdateAkun().observe(this@AdminSemuaAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminSemuaAkunActivity)
                is UIState.Failure-> setFailureUpdateAkun(result.message)
                is UIState.Success-> setSuccessUpdateAkun(result.data)
            }
        }
    }

    private fun setSuccessUpdateAkun(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchSemuaAkun()
            } else{
                Toast.makeText(this@AdminSemuaAkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminSemuaAkunActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureUpdateAkun(message: String) {
        Toast.makeText(this@AdminSemuaAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(akun: UsersModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus ${akun.nama!!} ?"
            tvBodyKonfirmasi.text = "Akun ini akan hapus dan data tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                val idUser = akun.idUser!!
                postHapusUser(idUser)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusUser(idUser: String){
        viewModel.postDeleteAkun(idUser!!)
    }

    private fun getHapusUser(){
        viewModel.getDeleteAkun().observe(this@AdminSemuaAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminSemuaAkunActivity)
                is UIState.Failure-> setFailureHapusAkun(result.message)
                is UIState.Success-> setSuccessHapusAkun(result.data)
            }
        }
    }

    private fun setSuccessHapusAkun(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchSemuaAkun()
            } else{
                Toast.makeText(this@AdminSemuaAkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminSemuaAkunActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureHapusAkun(message: String) {
        Toast.makeText(this@AdminSemuaAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }
}