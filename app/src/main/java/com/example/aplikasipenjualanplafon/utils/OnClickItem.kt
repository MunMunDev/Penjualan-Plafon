package com.example.aplikasipenjualanplafon.utils

import android.view.View
import com.example.aplikasipenjualanplafon.data.model.AlamatModel
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.data.model.TestimoniModel
import com.example.aplikasipenjualanplafon.data.model.UsersModel

interface OnClickItem {
    interface ClickJenisPlafon{
        fun clickItemSetting(jenisPlafon: JenisPlafonModel, it: View)
        fun clickItemKeunggulan(jenisPlafon: JenisPlafonModel, it: View)
    }

    interface ClickPlafon{
        fun clickItemDetail(plafon: PlafonModel, it: View)
        fun clickItemPlafon(plafon: PlafonModel, it: View)
        fun clickItemImage(jenisPlafon:String, image: String)
    }

    interface ClickPesanan{
        fun clickItemPesanan(pesanan: PesananModel, it: View)
        fun clickGambarPesanan(gambar: String, jenisPlafon:String, it: View)
    }

    interface ClickRiwayatPesanan{
        fun clickItem(idPemesanan: String, it: View)
    }

    interface ClickRiwayatPesananDetail{
        fun clickItemPesanan(pesanan: RiwayatPesananValModel, it: View)
        fun clickGambarPesanan(gambar: String, jenisPlafon:String, it: View)
        fun clicTestimoni(valueIdPemesanan:String, valueIdPlafon:String, valueJenisPlafon:String, it: View)
    }

    interface ClickPilihAlamat{
        fun clickItemPilih(data: AlamatModel, it: View)
        fun clickItemEdit(data:AlamatModel, it: View)
    }

    interface ClickAdminDetailPesanan{
        fun clickItemSetting(pesanan: RiwayatPesananValModel, no:String, it: View)
        fun clickAlamatPesanan(alamat: String, it: View)
        fun clickGambarPesanan(gambar: String, jenisPlafon:String, it: View)
    }

    interface ClickAdminKeranjangBelanja{
        fun clickItemSetting(idPemesanan: String, nama: String, it: View)
        fun clickPesanan(idUser: String, nama: String, it: View)
    }

    interface ClickAdminPesanan{
        fun clickItemSetting(idPemesanan: String, nama: String, it: View)
        fun clickPesanan(idPemesanan: String, nama: String, it: View)
    }

    interface ClickAdminPesananDetail{
        fun clickItemSetting(idPemesanan: String, nama: String, it: View)
//        fun clickPesanan(idPemesanan: String, nama: String, it: View)
    }

    interface ClickAdminRiwayatPesanan{
        fun clickItemSetting(idPemesanan: String, nama: String, it: View)
        fun clickPesanan(idUser: String, nama: String, it: View)
    }

    interface ClickAdminRiwayatPesananDetail{
        fun clickItemSetting(pesanan: RiwayatPesananValModel, it: View)
        fun clickKeterangan(keterangan: String, isi: String, it: View)
        fun clickJenisPlafon(jenisPlafon: String, it: View)
        fun clickGambarPesanan(gambar: String, jenisPlafon:String, it: View)
    }

    interface ClickAkun{
        fun clickItemSetting(akun: UsersModel, it: View)
//        fun clickItemNama(nama: String, it: View)
        fun clickItemAlamat(alamat: String, it: View)
    }
}