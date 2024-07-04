package com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.adapter.RiwayatPesananAdapter
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananHalModel
import com.example.aplikasipenjualanplafon.databinding.ActivityRiwayatPesananBinding
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiwayatPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPesananBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: RiwayatPesananViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setSharedPreferencesLogin()
        fetchRiwayatPembayaran()
        getRiwayatPembayaran()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@RiwayatPesananActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@RiwayatPesananActivity)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@RiwayatPesananActivity)
    }

    private fun fetchRiwayatPembayaran() {
        viewModel.fetchRiwayatPesanan(sharedPreferencesLogin.getIdUser().toString())
    }
    private fun getRiwayatPembayaran(){
        viewModel.getRiwayatPesanan().observe(this@RiwayatPesananActivity){ result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchRiwayatPembayaran(result.data)
                is UIState.Failure-> setFailureFetchRiwayatPembayaran(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayatPembayaran(message: String) {
        Toast.makeText(this@RiwayatPesananActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchRiwayatPembayaran(data: ArrayList<RiwayatPesananHalModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@RiwayatPesananActivity, "Tidak ada data Jenis Plafon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: java.util.ArrayList<RiwayatPesananHalModel>) {
        val adapter = RiwayatPesananAdapter(data, object : OnClickItem.ClickRiwayatPesanan{
            override fun clickItem(idPemesanan: String, it: View) {
                val i = Intent(this@RiwayatPesananActivity, RiwayatPesananDetailActivity::class.java)
                i.putExtra("idPemesanan", idPemesanan)
                startActivity(i)
            }

        })
        binding.apply {
            rvRiwayatPesanan.layoutManager = LinearLayoutManager(this@RiwayatPesananActivity, LinearLayoutManager.VERTICAL, false)
            rvRiwayatPesanan.adapter = adapter
        }
    }
}