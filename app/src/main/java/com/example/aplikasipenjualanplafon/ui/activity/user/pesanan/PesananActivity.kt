package com.example.aplikasipenjualanplafon.ui.activity.user.pesanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.RiwayatPesananAdapter
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananHalModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPesananBinding
import com.example.aplikasipenjualanplafon.databinding.ActivityRiwayatPesananBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan.RiwayatPesananDetailActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan.RiwayatPesananViewModel
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import com.example.aplikasipenjualanplafon.utils.OnClickItem
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PesananViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setSharedPreferencesLogin()
        fetchRiwayatPembayaran()
        getRiwayatPembayaran()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@PesananActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@PesananActivity)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PesananActivity)
    }

    private fun fetchRiwayatPembayaran() {
        viewModel.fetchPesanan(sharedPreferencesLogin.getIdUser().toString())
    }
    private fun getRiwayatPembayaran(){
        viewModel.getPesanan().observe(this@PesananActivity){ result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchRiwayatPembayaran(result.data)
                is UIState.Failure-> setFailureFetchRiwayatPembayaran(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayatPembayaran(message: String) {
        Toast.makeText(this@PesananActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchRiwayatPembayaran(data: ArrayList<RiwayatPesananHalModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@PesananActivity, "Tidak ada data Jenis Plafon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: java.util.ArrayList<RiwayatPesananHalModel>) {
        val adapter = RiwayatPesananAdapter(data, object : OnClickItem.ClickRiwayatPesanan{
            override fun clickItem(idPemesanan: String, it: View) {
                val i = Intent(this@PesananActivity, PesananDetailActivity::class.java)
                i.putExtra("idPemesanan", idPemesanan)
                startActivity(i)
            }

        })
        binding.apply {
            rvRiwayatPesanan.layoutManager = LinearLayoutManager(this@PesananActivity, LinearLayoutManager.VERTICAL, false)
            rvRiwayatPesanan.adapter = adapter
        }
    }
}