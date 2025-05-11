package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class KabKotaModel (
    @SerializedName("id_kab_kota")
    var id_kab_kota: String? = null,

    @SerializedName("id_provinsi")
    var id_provinsi: String? = null,

    @SerializedName("kab_kota")
    var kab_kota: String? = null,

    @SerializedName("kecamatan")
    var listKecamatan: KecamatanModel? = null
)