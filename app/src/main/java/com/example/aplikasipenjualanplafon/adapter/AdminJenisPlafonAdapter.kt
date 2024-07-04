package com.example.aplikasipenjualanplafon.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.databinding.ListAdminJenisPlafonBinding
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.OnClickItem

class AdminJenisPlafonAdapter(
    private var listJenisPlafon: ArrayList<JenisPlafonModel>,
    private var onClick: OnClickItem.ClickJenisPlafon
): RecyclerView.Adapter<AdminJenisPlafonAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminJenisPlafonBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminJenisPlafonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listJenisPlafon.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJenisPlafon.text = "Jenis Plafon"
                tvKeunggulan.text = "Keunggulan"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisPlafon.setBackgroundResource(R.drawable.bg_table_title)
                tvKeunggulan.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisPlafon.setTextColor(Color.parseColor("#ffffff"))
                tvKeunggulan.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisPlafon.setTypeface(null, Typeface.BOLD)
                tvKeunggulan.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val jenisPlafon = listJenisPlafon[(position-1)]

                tvNo.text = "$position"
                tvJenisPlafon.text = jenisPlafon.jenis_plafon
                tvKeunggulan.text = jenisPlafon.keunggulan
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisPlafon.setBackgroundResource(R.drawable.bg_table)
                tvKeunggulan.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisPlafon.setTextColor(Color.parseColor("#000000"))
                tvKeunggulan.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisPlafon.setTypeface(null, Typeface.NORMAL)
                tvKeunggulan.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvKeunggulan.gravity = Gravity.CENTER_VERTICAL

                tvKeunggulan.setOnClickListener{
                    onClick.clickItemKeunggulan(jenisPlafon, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(jenisPlafon, it)
                }
            }
        }
    }
}