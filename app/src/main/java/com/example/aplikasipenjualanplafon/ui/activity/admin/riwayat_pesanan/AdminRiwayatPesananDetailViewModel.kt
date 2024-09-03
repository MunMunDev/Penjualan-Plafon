package com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.AdminPesananDetailModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRiwayatPesananDetailViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<AdminPesananDetailModel>>>()
    private var _jenisPlafon = MutableLiveData<UIState<ArrayList<PlafonModel>>>()
    private var _postTambahRiwayatPesananDetail = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdateRiwayatPesananDetail = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postDeleteRiwayatPesananDetail = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchPesanan(id_user: String){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminRiwayatPesananDetail("", id_user)
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchPlafon(){
        viewModelScope.launch(Dispatchers.IO) {
            _jenisPlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAllPlafon("")
                _jenisPlafon.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _jenisPlafon.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahRiwayatPesananDetail(
        idUser:String, idPemesanan:String, idPlafon:String, nama_lengkap:String, nomor_hp:String,
        kecamatan:String, alamat:String, detail_alamat:String, jumlah:String, metodePembayaran:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahRiwayatPesananDetail.postValue(UIState.Loading)
            delay(1_000)
            try {
//                val fetchRiwayatPesanan = api.postTambahRiwayatPesananDetail(
//                    "", idUser, idPemesanan, idPlafon, alamat,
//                    jumlah, harga, totalHarga, metodePembayaran, waktu
//                )
                val fetchRiwayatPesanan = api.postTambahRiwayatPesananDetail(
                    "", idUser, idPemesanan, idPlafon, nama_lengkap, nomor_hp,
                    kecamatan, alamat, detail_alamat, jumlah, metodePembayaran
                )
                _postTambahRiwayatPesananDetail.postValue(UIState.Success(fetchRiwayatPesanan))
            } catch (ex: Exception){
                _postTambahRiwayatPesananDetail.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpdateRiwayatPesanan(
        idRiwayatPesanan:String, idUser:String, idPemesanan:String, idPlafon:String, namaLengkap:String,
        nomorHp:String, kecamatan:String, alamat:String, detailAlamat:String, jumlah:String, metodePembayaran:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateRiwayatPesananDetail.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchRiwayatPesanan = api.postUpdateRiwayatPesananDetail(
                    "", idRiwayatPesanan, idUser, idPemesanan,
                    idPlafon, namaLengkap, nomorHp, kecamatan, alamat, detailAlamat, jumlah, metodePembayaran
                )
                _postUpdateRiwayatPesananDetail.postValue(UIState.Success(fetchRiwayatPesanan))
            } catch (ex: Exception){
                _postUpdateRiwayatPesananDetail.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postHapusRiwayatPesanan(idRiwayatPesanan:String){
        viewModelScope.launch(Dispatchers.IO) {
            _postDeleteRiwayatPesananDetail.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchRiwayatPesanan = api.postDeleteRiwayatPesananDetail("", idRiwayatPesanan)
                _postDeleteRiwayatPesananDetail.postValue(UIState.Success(fetchRiwayatPesanan))
            } catch (ex: Exception){
                _postDeleteRiwayatPesananDetail.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<AdminPesananDetailModel>>> = _pesanan
    fun getPlafon(): LiveData<UIState<ArrayList<PlafonModel>>> = _jenisPlafon
    fun getTambahRiwayatPesananDetail(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahRiwayatPesananDetail
    fun getUpdateRiwayatPesananDetail(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateRiwayatPesananDetail
    fun getDeleteRiwayatPesananDetail(): LiveData<UIState<ArrayList<ResponseModel>>> = _postDeleteRiwayatPesananDetail
}