package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class KecamatanModel (
    @SerializedName("id_kecamatan")
    var id_kecamatan: String? = null,

    @SerializedName("id_kab_kota")
    var id_kab_kota: String? = null,

    @SerializedName("kecamatan")
    var kecamatan: String? = null,
)