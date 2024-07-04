package com.example.aplikasipenjualanplafon.ui.activity.admin.jenis_plafon

import android.content.Intent
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
import com.example.aplikasipenjualanplafon.adapter.AdminJenisPlafonAdapter
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminJenisPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogJenisPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKeteranganBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasipenjualanplafon.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminJenisPlafonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminJenisPlafonBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminJenisPlafonViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminJenisPlafonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminJenisPlafonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchJenisPlafon()
        getJenisPlafon()
        getTambahJenisPlafon()
        getUpdateJenisPlafon()
        getHapusJenisPlafon()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminJenisPlafonActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminJenisPlafonActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
        }
    }

    private fun fetchJenisPlafon() {
        viewModel.fetchJenisPlafon()
    }
    private fun getJenisPlafon(){
        viewModel.getJenisPlafon().observe(this@AdminJenisPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisPlafonActivity)
                is UIState.Failure-> setFailureFetchJenisPlafon(result.message)
                is UIState.Success-> setSuccessFetchJenisPlafon(result.data)
            }
        }
    }

    private fun setFailureFetchJenisPlafon(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminJenisPlafonActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisPlafon(data: ArrayList<JenisPlafonModel>) {
        if(data.isNotEmpty()){
            setAdapterJenisPlafon(data)
        } else{
            Toast.makeText(this@AdminJenisPlafonActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setAdapterJenisPlafon(data: ArrayList<JenisPlafonModel>) {
        adapter = AdminJenisPlafonAdapter(data, object : OnClickItem.ClickJenisPlafon{
            override fun clickItemSetting(jenisPlafon: JenisPlafonModel, it: View) {
                val popupMenu = PopupMenu(this@AdminJenisPlafonActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(jenisPlafon)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(jenisPlafon)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickItemKeunggulan(jenisPlafon: JenisPlafonModel, it: View) {
                showKeteranganJenisPlafon(jenisPlafon)
            }
        })

        binding.apply {
            rvJenisPlafon.layoutManager = LinearLayoutManager(this@AdminJenisPlafonActivity, LinearLayoutManager.VERTICAL, false)
            rvJenisPlafon.adapter = adapter
        }
    }

    private fun showKeteranganJenisPlafon(jenisPlafon: JenisPlafonModel) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = jenisPlafon.jenis_plafon
            tvBodyKeterangan.text = jenisPlafon.keunggulan

            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(jenisPlafon: JenisPlafonModel) {
        val view = AlertDialogJenisPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etJenisPlafon.setText(jenisPlafon.jenis_plafon)
            etKeunggulanPlafon.setText(jenisPlafon.keunggulan)

            var cek = true
            if(etJenisPlafon.text.toString().trim().isEmpty()){
                etJenisPlafon.error = "Tidak Boleh Kosong"
                cek = false
            }
            if(etKeunggulanPlafon.text.toString().trim().isEmpty()){
                etJenisPlafon.error = "Tidak Boleh Kosong"
                cek = false
            }

            btnSimpan.setOnClickListener {
                if(cek){
                    postUpdateJenisPlafon(jenisPlafon.id_jenis_plafon!!, etJenisPlafon.text.toString().trim(), etKeunggulanPlafon.text.toString().trim())

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateJenisPlafon(idJenisPlafon: String, jenisPlafon: String, keunggulan: String) {
        viewModel.postUpdatePlafon(idJenisPlafon, jenisPlafon, keunggulan)
    }

    private fun getUpdateJenisPlafon(){
        viewModel.getUpdatePlafon().observe(this@AdminJenisPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisPlafonActivity)
                is UIState.Failure-> setFailureUpdateJenisPlafon(result.message)
                is UIState.Success-> setSuccessUpdateJenisPlafon(result.data)
            }
        }
    }

    private fun setFailureUpdateJenisPlafon(message: String) {
        Toast.makeText(this@AdminJenisPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateJenisPlafon(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminJenisPlafonActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                fetchJenisPlafon()
            } else{
                Toast.makeText(this@AdminJenisPlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisPlafonActivity, "gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShowDialogHapus(jenisPlafon: JenisPlafonModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus ${jenisPlafon.jenis_plafon}"
            tvBodyKonfirmasi.text = "Jenis Plafon ini akan menghapus plafon yang terkait dengan jenis plafon ini"

            btnKonfirmasi.setOnClickListener {
                postHapusJenisPlafon(jenisPlafon.id_jenis_plafon!!)
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusJenisPlafon(idJenisPlafon: String) {
        viewModel.postDeletePlafon(idJenisPlafon)
    }

    private fun getHapusJenisPlafon(){
        viewModel.getDeletePlafon().observe(this@AdminJenisPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisPlafonActivity)
                is UIState.Failure-> setFailureDeleteJenisPlafon(result.message)
                is UIState.Success-> setSuccessDeleteJenisPlafon(result.data)
            }
        }
    }

    private fun setSuccessDeleteJenisPlafon(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminJenisPlafonActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchJenisPlafon()
            } else{
                Toast.makeText(this@AdminJenisPlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisPlafonActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureDeleteJenisPlafon(message: String) {
        Toast.makeText(this@AdminJenisPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogTambah() {
        val view = AlertDialogJenisPlafonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisPlafonActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnSimpan.setOnClickListener {
                var cek = true
                if(etJenisPlafon.text.toString().trim().isEmpty()){
                    etJenisPlafon.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etKeunggulanPlafon.text.toString().trim().isEmpty()){
                    etKeunggulanPlafon.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(cek){
                    postTambahJenisPlafon(etJenisPlafon.text.toString().trim(), etKeunggulanPlafon.text.toString().trim())

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahJenisPlafon(jenisPlafon: String, keunggulan: String) {
        viewModel.postTambahPlafon(jenisPlafon, keunggulan)
    }

    private fun getTambahJenisPlafon(){
        viewModel.getTambahPlafon().observe(this@AdminJenisPlafonActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisPlafonActivity)
                is UIState.Failure-> setFailureTambahJenisPlafon(result.message)
                is UIState.Success-> setSuccessTambahJenisPlafon(result.data)
            }
        }
    }

    private fun setFailureTambahJenisPlafon(message: String) {
        Toast.makeText(this@AdminJenisPlafonActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahJenisPlafon(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminJenisPlafonActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                fetchJenisPlafon()
            } else{
                Toast.makeText(this@AdminJenisPlafonActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminJenisPlafonActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminJenisPlafonActivity, AdminMainActivity::class.java))
        finish()
    }
}