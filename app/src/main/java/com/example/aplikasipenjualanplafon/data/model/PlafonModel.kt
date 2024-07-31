package com.example.aplikasipenjualanplafon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PlafonModel (
    @SerializedName("id_plafon")
    val id_plafon: String? = null,

    @SerializedName("jenis_plafon")
    val jenis_plafon: ArrayList<JenisPlafonModel>? = null,

    @SerializedName("keterangan")
    val keterangan: String? = null,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("stok")
    val stok: String? = null,

    @SerializedName("ukuran")
    val ukuran: String? = null,

    @SerializedName("harga")
    val harga: String? = null,

    @SerializedName("rekomendasi")
    val rekomendasi: String? = null
)