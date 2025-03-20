package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class ProvinsiModel (
    @SerializedName("provinsi")
    var provinsi: String? = null,

    @SerializedName("kab_kota")
    var listKabKota: ArrayList<KabKotaModel>? = null
)