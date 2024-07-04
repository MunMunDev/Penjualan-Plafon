package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class JenisPlafonModel (
    @SerializedName("id_jenis_plafon")
    val id_jenis_plafon: String? = null,

    @SerializedName("jenis_plafon")
    val jenis_plafon: String? = null,

    @SerializedName("keunggulan")
    val keunggulan: String? = null

)