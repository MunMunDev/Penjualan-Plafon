package com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRiwayatPesananViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<ListPesananModel>>>()
    private var _postTambahRiwayatPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdateRiwayatPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postDeleteRiwayatPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

//    fun fetchRiwayatPesanan(){
//        viewModelScope.launch(Dispatchers.IO) {
//            _pesanan.postValue(UIState.Loading)
//            delay(1_000)
//            try {
//                val fetchRiwayatPesanan = api.getAdminRiwayatPesanan("")
//                _pesanan.postValue(UIState.Success(fetchRiwayatPesanan))
//            } catch (ex: Exception){
//                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
//            }
//        }
//    }

    fun fetchRiwayatPesanan(){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminRiwayatPesanan("", "2")
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahRiwayatPesanan(
        id_pemesanan: String, id_user: String, nama: String, alamat: String, nomor_hp: String, jenis_plafon: String, harga: String,
        panjang: String, lebar: String, total_harga: String, waktu: String, waktu_konfirmasi: String, metode_pembayaran: String, waktu_bayar: String
    ){
        viewModelScope.launch {
            _postTambahRiwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahRiwayatPesanan = api.postTambahRiwayatPesanan(
                    "", id_pemesanan, id_user, nama, alamat, nomor_hp, jenis_plafon, harga, panjang, lebar,
                    total_harga, waktu, waktu_konfirmasi, metode_pembayaran, waktu_bayar
                )
                _postTambahRiwayatPesanan.postValue(UIState.Success(postTambahRiwayatPesanan))
            } catch (ex: Exception){
                _postTambahRiwayatPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateRiwayatPesanan(
        id_pesanan:String,  id_user:String, nama:String, alamat:String, nomor_hp:String, jenis_plafon:String, harga:String,
        panjang:String, lebar:String, total_harga:String, gambar:String, waktu:String,
        waktu_konfirmasi:String, metode_pembayaran:String, waktu_bayar:String
    ){
        viewModelScope.launch {
            _postUpdateRiwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postUpdateRiwayatPesanan = api.postUpdateRiwayatPesanan(
                    "", id_pesanan, id_user, nama, alamat, nomor_hp, jenis_plafon, harga, panjang,
                    lebar, total_harga, gambar, waktu, waktu_konfirmasi, metode_pembayaran, waktu_bayar
                )
                _postUpdateRiwayatPesanan.postValue(UIState.Success(postUpdateRiwayatPesanan))
            } catch (ex: Exception){
                _postUpdateRiwayatPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteRiwayatPesanan(idRiwayatPesanan: String){
        viewModelScope.launch {
            _postDeleteRiwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postDeleteRiwayatPesanan = api.postDeleteRiwayatPesanan(
                    "", idRiwayatPesanan
                )
                _postDeleteRiwayatPesanan.postValue(UIState.Success(postDeleteRiwayatPesanan))
            } catch (ex: Exception){
                _postDeleteRiwayatPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getRiwayatPesanan(): LiveData<UIState<ArrayList<ListPesananModel>>> = _pesanan
    fun getTambahRiwayatPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahRiwayatPesanan
    fun getUpdateRiwayatPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateRiwayatPesanan
    fun getDeleteRiwayatPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postDeleteRiwayatPesanan
}