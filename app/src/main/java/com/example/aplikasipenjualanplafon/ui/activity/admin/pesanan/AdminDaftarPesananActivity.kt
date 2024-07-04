package com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminDaftarPesananBinding
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDaftarPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDaftarPesananBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDaftarPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()

    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminDaftarPesananActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminDaftarPesananActivity)
        }
    }

    private fun setButton() {
        binding.apply {
//            btnKonfirmasiPesanan.setOnClickListener {
//                val i = Intent(this@AdminDaftarPesananActivity, AdminPesananActivity::class.java)
//                i.putExtra("pilihan", "konfirmasi")
//                startActivity(i)
//            }
//            btnPesanan.setOnClickListener {
//                val i = Intent(this@AdminDaftarPesananActivity, AdminPesananActivity::class.java)
//                i.putExtra("pilihan", "pesanan")
//                startActivity(i)
//            }
//            btnSemua.setOnClickListener {
//                val i = Intent(this@AdminDaftarPesananActivity, AdminDetailPesananActivity::class.java)
//                startActivity(i)
//            }
        }
    }
}