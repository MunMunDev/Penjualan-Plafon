package com.example.aplikasipenjualanplafon.data.model

import com.google.gson.annotations.SerializedName

class KeranjangBelanjaDetailModel (
    @SerializedName("id_riwayat_pesanan")
    val id_riwayat_pesanan: String? = null,

    @SerializedName("id_user")
    val id_user: String? = null,

    @SerializedName("user")
    val user: ArrayList<UsersModel>? = null,

    @SerializedName("id_plafon")
    val id_plafon: String? = null,

    @SerializedName("id_pemesanan")
    val id_pemesanan: String? = null,

    @SerializedName("alamat")
    val alamat: String? = null,

    @SerializedName("jenis_plafon")
    val jenis_plafon: String? = null,

    @SerializedName("ukuran")
    val ukuran: String? = null,

    @SerializedName("jumlah")
    val jumlah: String? = null,

    @SerializedName("harga")
    val harga: String? = null,

    @SerializedName("total_harga")
    val total_harga: String? = null,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("metode_pembayaran")
    val metode_pembayaran: String? = null,

    @SerializedName("waktu")
    val waktu: String? = null

)