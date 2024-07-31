package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class KecamatanModel (
    @SerializedName("id_kecamatan")
    var id_kecamatan: String? = null,

    @SerializedName("kecamatan")
    var kecamatan: String? = null,
)