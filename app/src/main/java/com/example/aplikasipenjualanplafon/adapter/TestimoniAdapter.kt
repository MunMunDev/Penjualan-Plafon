package com.example.aplikasipenjualanplafon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.TestimoniModel
import com.example.aplikasipenjualanplafon.databinding.ListTestimoniBinding
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu
import java.lang.Exception

class TestimoniAdapter(
    private val listTestimoni: ArrayList<TestimoniModel>,
    private val idUser: String
): RecyclerView.Adapter<TestimoniAdapter.ViewHolder>() {

    private val tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListTestimoniBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListTestimoniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTestimoni.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testimoni = listTestimoni[position]
        holder.binding.apply {
            var nama = testimoni.nama_user!!
            val tanggal = tanggalDanWaktu.konversiBulan(testimoni.tanggal!!)
            val valueTestimoni = testimoni.testimoni!!
            val bintang = testimoni.bintang!!.trim().toInt()

            var firstLetter = try {
                nama.substring(0, 1)
            } catch (ex: Exception){
                "A"
            }

            if(idUser == testimoni.id_user){
                firstLetter = "A"
                nama+=" (Anda)"
            }

            tvNama.text = nama
            tvTanggal.text = tanggal
            tvTestimoni.text = valueTestimoni
            tvInisial.text = firstLetter

            val listBgCircle = ArrayList<Int>()
            listBgCircle.add(R.drawable.bg_circle)
            listBgCircle.add(R.drawable.bg_circle_1)
            listBgCircle.add(R.drawable.bg_circle_2)
            listBgCircle.add(R.drawable.bg_circle_3)
            listBgCircle.add(R.drawable.bg_circle_4)
            listBgCircle.add(R.drawable.bg_circle_5)

            when (bintang) {
                1 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                }
                2 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                }
                3 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang3.setImageResource(R.drawable.icon_star_aktif)
                }
                4 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang3.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang4.setImageResource(R.drawable.icon_star_aktif)
                }
                5 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang3.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang4.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang5.setImageResource(R.drawable.icon_star_aktif)
                }
            }

            val mathRandom = (Math.random()*5).toInt()
            tvInisial.setBackgroundResource(listBgCircle[mathRandom])

        }
    }
}