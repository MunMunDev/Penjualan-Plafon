package com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.AdminListPesananAdapter
import com.example.aplikasipenjualanplafon.adapter.AdminRiwayatPesananListAdapter
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminPesananBinding
import com.example.aplikasipenjualanplafon.databinding.ActivityAdminRiwayatPesananBinding
import com.example.aplikasipenjualanplafon.databinding.AlertDialogAdminPrintLaporanBinding
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
        setButton()
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

    private fun setButton() {
        binding.apply {
            btnPrintLaporan.setOnClickListener {
                setShowDialogPrintLaporan()
            }
        }
    }

    private fun setShowDialogPrintLaporan() {
        val view = AlertDialogAdminPrintLaporanBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        var numberPosition = 0
        var selectedValue = ""

        view.apply {
            // Spinner Metode Pembayaran
            val arrayAdapter = ArrayAdapter.createFromResource(
                this@AdminRiwayatPesananActivity,
                R.array.print_laporan,
                android.R.layout.simple_spinner_item
            )

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPrintLaporan.adapter = arrayAdapter

            spPrintLaporan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spPrintLaporan.selectedItemPosition
                    selectedValue = spPrintLaporan.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spPrintLaporan.adapter = arrayAdapter

            btnPrint.setOnClickListener {
                if(numberPosition==0){
                    val i = Intent(this@AdminRiwayatPesananActivity, AdminPrintLaporanActivity::class.java)
                    i.putExtra("print_laporan", "online")
                    startActivity(i)
                } else{
                    val i = Intent(this@AdminRiwayatPesananActivity, AdminPrintLaporanActivity::class.java)
                    i.putExtra("print_laporan", "ditempat")
                    startActivity(i)
                }
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
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
        Log.d("DetailTAG", ": $message ")
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