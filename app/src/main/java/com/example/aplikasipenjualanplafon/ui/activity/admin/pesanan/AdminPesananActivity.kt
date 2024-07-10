package com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan

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
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminListPesananAdapter
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminPesananBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogAdminPesananBinding
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
class AdminPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPesananBinding
    private val viewModel: AdminPesananViewModel by viewModels()
    private lateinit var adapter: AdminListPesananAdapter
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var checkData: String
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchData()
        getPesanan()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPesananActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPesananActivity)
        }
    }



    private fun fetchData() {
        viewModel.fetchPesanan()
    }
    private fun getPesanan(){
        viewModel.getPesanan().observe(this@AdminPesananActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPesananActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminPesananActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<ListPesananModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminPesananActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<ListPesananModel>) {
        adapter = AdminListPesananAdapter(data, object: OnClickItem.ClickAdminPesanan{
            override fun clickItemSetting(idPemesanan: String, nama: String, it: View) {

            }

            override fun clickPesanan(idPemesanan: String, nama: String, it: View) {
                val i = Intent(this@AdminPesananActivity, AdminDetailPesananActivity::class.java)
                i.putExtra("idPemesanan", idPemesanan)
                i.putExtra("nama", nama)
                startActivity(i)
            }

        })

        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(
                this@AdminPesananActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPesanan.adapter = adapter
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminPesananActivity, AdminMainActivity::class.java))
        finish()
    }
}