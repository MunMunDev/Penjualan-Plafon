package com.example.aplikasipenjualanplafon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class KonfirmasiPesananModel (
    @SerializedName("id_pemesanan")
    val id_pemesanan: String? = null,

    @SerializedName("jumlah_jenis_plafon")
    val jumlah_jenis_plafon: String? = null,

    @SerializedName("total_harga")
    val total_harga: String? = null,

    @SerializedName("waktu")
    val waktu: String? = null

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_pemesanan)
        parcel.writeString(jumlah_jenis_plafon)
        parcel.writeString(total_harga)
        parcel.writeString(waktu)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KonfirmasiPesananModel> {
        override fun createFromParcel(parcel: Parcel): KonfirmasiPesananModel {
            return KonfirmasiPesananModel(parcel)
        }

        override fun newArray(size: Int): Array<KonfirmasiPesananModel?> {
            return arrayOfNulls(size)
        }
    }
}