package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class AdminPesananDetailModel (
    @SerializedName("id_user") var id_user : String? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("nomor_hp") var nomorHp    : String? = null,
    @SerializedName("metode_pembayaran") var metodePembayaran : String? = null,
    @SerializedName("ket") var ket : String? = null,
    @SerializedName("total_harga" ) var totalHarga : String? = null,
    @SerializedName("waktu" ) var waktu: String? = null,
    @SerializedName("pesanan") var pesanan : ArrayList<RiwayatPesananValModel> = arrayListOf()
)