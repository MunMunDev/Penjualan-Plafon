package com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminListPesananAdapter
import com.example.aplikasipenjualanplafon.adapter.AdminRiwayatPesananListAdapter
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminPesananBinding
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminRiwayatPesananBinding
import com.example.aplikasipenjualanplafon.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminDetailPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminPesananViewModel
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminRiwayatPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRiwayatPesananBinding
    private val viewModel: AdminRiwayatPesananViewModel by viewModels()
    private lateinit var adapter: AdminRiwayatPesananListAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchData()
        getPesanan()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminRiwayatPesananActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminRiwayatPesananActivity)
        }
    }


    private fun fetchData() {
        viewModel.fetchRiwayatPesanan()
    }
    private fun getPesanan(){
        viewModel.getRiwayatPesanan().observe(this@AdminRiwayatPesananActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminRiwayatPesananActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminRiwayatPesananActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<ListPesananModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminRiwayatPesananActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<ListPesananModel>) {
        adapter = AdminRiwayatPesananListAdapter(data, object: OnClickItem.ClickAdminRiwayatPesanan{
            override fun clickItemSetting(idPemesanan: String, nama: String, it: View) {

            }

            override fun clickPesanan(idUser: String, nama: String, it: View) {
                val i = Intent(this@AdminRiwayatPesananActivity, AdminRiwayatPesananDetailActivity::class.java)
                i.putExtra("idUser", idUser)
                i.putExtra("nama", nama)
                startActivity(i)
            }

        })

        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(
                this@AdminRiwayatPesananActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPesanan.adapter = adapter
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminRiwayatPesananActivity, AdminMainActivity::class.java))
        finish()
    }
}