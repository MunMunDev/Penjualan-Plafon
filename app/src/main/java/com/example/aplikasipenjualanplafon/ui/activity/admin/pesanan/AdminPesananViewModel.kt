package com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.UsersModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPesananViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
//    private var _pesanan = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    private var _pesanan = MutableLiveData<UIState<ArrayList<ListPesananModel>>>()
    private var _postTambahPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdatePesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postDeletePesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

//    fun fetchPesanan(){
//        viewModelScope.launch(Dispatchers.IO) {
//            _pesanan.postValue(UIState.Loading)
//            delay(1_000)
//            try {
//                val fetchPesanan = api.getAdminPesanan("")
//                _pesanan.postValue(UIState.Success(fetchPesanan))
//            } catch (ex: Exception){
//                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
//            }
//        }
//    }

    fun fetchPesanan(){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminPesanan("")
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

//    fun postTambahPesanan(
//        id_pemesanan: String, id_user: String, nama: String, alamat: String, nomor_hp: String, jenis_plafon: String, harga: String,
//        panjang: String, lebar: String, total_harga: String, waktu: String, waktu_konfirmasi: String, metode_pembayaran: String, waktu_bayar: String
//    ){
//        viewModelScope.launch {
//            _postTambahPesanan.postValue(UIState.Loading)
//            delay(1_000)
//            try {
//                val postTambahPesanan = api.postTambahPesanan(
//                    "", id_pemesanan, id_user, nama, alamat, nomor_hp, jenis_plafon, harga, panjang, lebar,
//                    total_harga, waktu, waktu_konfirmasi, metode_pembayaran, waktu_bayar
//                )
//                _postTambahPesanan.postValue(UIState.Success(postTambahPesanan))
//            } catch (ex: Exception){
//                _postTambahPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
//            }
//        }
//    }


    fun postTambahPesanan(
        id_pemesanan: String, nama: String, alamat: String, nomor_hp: String, jenis_plafon: String, harga: String,
        panjang: String, lebar: String, total_harga: String, waktu: String, waktu_konfirmasi: String
    ){
        viewModelScope.launch {
            _postTambahPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPesanan = api.postTambahPesananAdmin(
                    "", id_pemesanan, nama, alamat, nomor_hp, jenis_plafon, harga, panjang, lebar,
                    total_harga, waktu, waktu_konfirmasi
                )
                _postTambahPesanan.postValue(UIState.Success(postTambahPesanan))
            } catch (ex: Exception){
                _postTambahPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

//    fun postUpdatePesanan(
//        id_pesanan:String,  id_user:String, nama:String, alamat:String, nomor_hp:String, jenis_plafon:String, harga:String,
//        panjang:String, lebar:String, total_harga:String, gambar:String, waktu:String,
//        waktu_konfirmasi:String, metode_pembayaran:String, waktu_bayar:String
//    ){
//        viewModelScope.launch {
//            _postUpdatePesanan.postValue(UIState.Loading)
//            delay(1_000)
//            try {
//                val postUpdatePesanan = api.postUpdatePesanan(
//                    "", id_pesanan, id_user, nama, alamat, nomor_hp, jenis_plafon, harga, panjang,
//                    lebar, total_harga, gambar, waktu, waktu_konfirmasi, metode_pembayaran, waktu_bayar
//                )
//                _postUpdatePesanan.postValue(UIState.Success(postUpdatePesanan))
//            } catch (ex: Exception){
//                _postUpdatePesanan.postValue(UIState.Failure("Error: ${ex.message}"))
//            }
//        }
//    }

    fun postUpdatePesanan(
        id_pesanan:String, nama:String, alamat:String, nomor_hp:String, jenis_plafon:String, harga:String,
        panjang:String, lebar:String, total_harga:String, gambar:String, waktu:String,
        waktu_konfirmasi:String
    ){
        viewModelScope.launch {
            _postUpdatePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postUpdatePesanan = api.postUpdatePesanan(
                    "", id_pesanan, nama, alamat, nomor_hp, jenis_plafon, harga, panjang,
                    lebar, total_harga, gambar, waktu, waktu_konfirmasi
                )
                _postUpdatePesanan.postValue(UIState.Success(postUpdatePesanan))
            } catch (ex: Exception){
                _postUpdatePesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeletePesanan(idPesanan: String){
        viewModelScope.launch {
            _postDeletePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postDeletePesanan = api.postDeletePesanan(
                    "", idPesanan
                )
                _postDeletePesanan.postValue(UIState.Success(postDeletePesanan))
            } catch (ex: Exception){
                _postDeletePesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

//    fun getPesanan(): LiveData<UIState<ArrayList<PesananModel>>> = _pesanan
    fun getPesanan(): LiveData<UIState<ArrayList<ListPesananModel>>> = _pesanan
    fun getTambahPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahPesanan
    fun getUpdatePesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdatePesanan
    fun getDeletePesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postDeletePesanan
}