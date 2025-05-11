package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class ProvinsiModel (
    @SerializedName("id_provinsi")
    var id_provinsi: String? = null,

    @SerializedName("provinsi")
    var provinsi: String? = null,

    @SerializedName("kab_kota")
    var listKabKota: KabKotaModel? = null
)