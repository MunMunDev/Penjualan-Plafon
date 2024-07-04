package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class RiwayatPesananHalModel (
    @SerializedName("id_pemesanan")
    val idPemesanan: String? = null,

    @SerializedName("jumlah_jenis_plafon")
    val jumlah_jenis_plafon: String? = null,

    @SerializedName("total_harga")
    val total_harga: String? = null,

    @SerializedName("waktu")
    val waktu: String? = null,

    @SerializedName("ket")
    val ket: String? = null,

)