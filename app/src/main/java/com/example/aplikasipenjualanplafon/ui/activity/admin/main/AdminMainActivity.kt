package com.example.aplikasipenjualanplafon.ui.activity.admin.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminMainBinding
import com.example.aplikasipenjualanplafon.databinding.ActivityMainBinding
import com.example.aplikasipenjualanplafon.ui.activity.admin.jenis_plafon.AdminJenisPlafonActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.keranjang_belanja.AdminKeranjangBelanjaActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminDaftarPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.plafon.AdminPlafonActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan.AdminRiwayatPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan.AdminRiwayatPesananDetailActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.semua_akun.AdminSemuaAkunActivity
import com.example.aplikasipenjualanplafon.utils.KontrolNavigationDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()

    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            cvJenisPlafon.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminJenisPlafonActivity::class.java))
            }
            cvPlafon.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPlafonActivity::class.java))
            }
            cvKeranjangBelanja.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminKeranjangBelanjaActivity::class.java))
            }
            cvPesanan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPesananActivity::class.java))
            }
            cvRiwayatPesanan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminRiwayatPesananActivity::class.java))
            }
            cvAkun.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminSemuaAkunActivity::class.java))
            }
        }
    }
}