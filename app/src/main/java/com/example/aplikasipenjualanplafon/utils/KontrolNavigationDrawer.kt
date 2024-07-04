package com.example.aplikasipenjualanplafon.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.ui.activity.admin.jenis_plafon.AdminJenisPlafonActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.keranjang_belanja.AdminKeranjangBelanjaActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminDaftarPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan.AdminPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.plafon.AdminPlafonActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan.AdminRiwayatPesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.admin.semua_akun.AdminSemuaAkunActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.main.MainActivity
import com.example.aplikasipenjualanplafon.ui.activity.login.LoginActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.akun.AkunActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.pesanan.PesananActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.plafon.PlafonActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan.RiwayatPesananActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }
    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "user"){
                when(it.itemId){
                    R.id.userNavDrawerHome -> {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerPlafon -> {
                        val intent = Intent(context, PlafonActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerPesanan -> {
                        val intent = Intent(context, PesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerRiwayatPembayaran -> {
                        val intent = Intent(context, RiwayatPesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerAkun -> {
                        val intent = Intent(context, AkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userBtnKeluar ->{
                        logout(activity)
                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome -> {
                        val intent = Intent(context, AdminMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerJenisPlafon -> {
                        val intent = Intent(context, AdminJenisPlafonActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPlafon -> {
                        val intent = Intent(context, AdminPlafonActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerKeranjangBelanja -> {
                        val intent = Intent(context, AdminKeranjangBelanjaActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPesanan -> {
//                        val intent = Intent(context, AdminDaftarPesananActivity::class.java)
                        val intent = Intent(context, AdminPesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerRiwayatPesanan -> {
                        val intent = Intent(context, AdminRiwayatPesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerAkun -> {
                        val intent = Intent(context, AdminSemuaAkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminBtnKeluar ->{
                        logout(activity)
                    }
                }

            }
            navigationLayout.setBackgroundColor(R.color.white)
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        sharedPreferences.setLogin(0, "", "","", "","", "")
        context.startActivity(Intent(context, LoginActivity::class.java))
        activity.finish()

    }
}