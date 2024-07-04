package com.example.aplikasipenjualanplafon.ui.activity.admin.keranjang_belanja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.adapter.AdminKeranjangBelanjaAdapter
import com.example.aplikasipenjualanplafon.adapter.AdminListPesananAdapter
import com.example.aplikasipenjualanplafon.data.model.ListKeranjangBelanjaModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminKeranjangBelanjaBinding
import com.example.aplikasipenjualanplafon.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminDetailPesananActivity
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminKeranjangBelanjaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminKeranjangBelanjaBinding
    private val viewModel: AdminKeranjangBelanjaViewModel by viewModels()
    private lateinit var adapter: AdminKeranjangBelanjaAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var checkData: String
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminKeranjangBelanjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchData()
        getPesanan()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminKeranjangBelanjaActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminKeranjangBelanjaActivity)
        }
    }

    private fun fetchData() {
        viewModel.fetchPesanan()
    }
    private fun getPesanan(){
        viewModel.getPesanan().observe(this@AdminKeranjangBelanjaActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminKeranjangBelanjaActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminKeranjangBelanjaActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<ListKeranjangBelanjaModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminKeranjangBelanjaActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<ListKeranjangBelanjaModel>) {
        adapter = AdminKeranjangBelanjaAdapter(data, object: OnClickItem.ClickAdminKeranjangBelanja{
            override fun clickItemSetting(idPemesanan: String, nama: String, it: View) {

            }

            override fun clickPesanan(idUser: String, nama: String, it: View) {
                val i = Intent(this@AdminKeranjangBelanjaActivity, AdminKeranjangBelanjaDetailActivity::class.java)
                i.putExtra("idUser", idUser)
                i.putExtra("nama", nama)
                startActivity(i)
            }

        })

        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(
                this@AdminKeranjangBelanjaActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPesanan.adapter = adapter
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminKeranjangBelanjaActivity, AdminMainActivity::class.java))
        finish()
    }
}