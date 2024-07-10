package com.example.aplikasipenjualanplafon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.AlamatModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.databinding.ListPesananPlafonBinding
import com.example.aplikasipenjualanplafon.databinding.ListPilihAlamatBinding
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class PilihAlamatAdapter(
    private val list: ArrayList<AlamatModel>,
    private val click: OnClickItem.ClickPilihAlamat
): RecyclerView.Adapter<PilihAlamatAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListPilihAlamatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPilihAlamatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.binding.apply {
            tvNama.text = data.nama_lengkap
            tvNomorHp.text = data.nomor_hp
            tvAlamat.text = data.alamat
            tvAlamatDetail.text = data.detail_alamat

            if(data.main=="1"){
                tvTitleAlamatSekarang.visibility = View.VISIBLE
            } else{
                tvTitleAlamatSekarang.visibility = View.GONE
            }

            clAlamat.setOnClickListener {
                click.clickItemPilih(data, it)
            }
            btnUbah.setOnClickListener{
                click.clickItemEdit(data, it)
            }
        }

    }
}