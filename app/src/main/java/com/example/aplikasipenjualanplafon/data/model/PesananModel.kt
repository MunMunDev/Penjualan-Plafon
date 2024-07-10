package com.example.aplikasipenjualanplafon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PesananModel (
    @SerializedName("id_pesanan")
    val id_pesanan: String? = null,

    @SerializedName("id_user")
    val idUser: String? = null,

    @SerializedName("id_plafon")
    val id_plafon: String? = null,

    @SerializedName("jumlah")
    val jumlah: String? = null,

    @SerializedName("plafon")
    val plafon: ArrayList<PlafonModel>? = null,

    @SerializedName("alamat")
    val alamat: ArrayList<AlamatModel>? = null,

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_pesanan)
        parcel.writeString(idUser)
        parcel.writeString(id_plafon)
        parcel.writeString(jumlah)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PesananModel> {
        override fun createFromParcel(parcel: Parcel): PesananModel {
            return PesananModel(parcel)
        }

        override fun newArray(size: Int): Array<PesananModel?> {
            return arrayOfNulls(size)
        }
    }
}