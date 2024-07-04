package com.example.aplikasipenjualanplafon.ui.activity.admin.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.AdminPesananDetailModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDetailPesananViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<AdminPesananDetailModel>>>()
    private var _plafon = MutableLiveData<UIState<ArrayList<JenisPlafonModel>>>()
    private var _postUpdateKonfirmasiPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdateKonfirmasiSelesai = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdateRiwayatPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postHapusRiwayatPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchPesanan(id_pemesanan: String){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminPesananDetail("", id_pemesanan)
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpdateKonfirmasiPembayaran(idPemesanan: String){
        viewModelScope.launch(Dispatchers.IO){
            _postUpdateKonfirmasiPembayaran.postValue(UIState.Loading)
            delay(1_000)
            try{
                val post = api.postAdminUpdateKonrimasiPembayaran("", idPemesanan)
                _postUpdateKonfirmasiPembayaran.postValue(UIState.Success(post))
            } catch (ex: Exception){
                _postUpdateKonfirmasiPembayaran.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateKonfirmasiSelesai(idPemesanan: String){
        viewModelScope.launch(Dispatchers.IO){
            _postUpdateKonfirmasiSelesai.postValue(UIState.Loading)
            delay(1_000)
            try{
                val post = api.postAdminUpdateKonrimasiSelesai("", idPemesanan)
                _postUpdateKonfirmasiSelesai.postValue(UIState.Success(post))
            } catch (ex: Exception){
                _postUpdateKonfirmasiSelesai.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }


    fun getPesanan(): LiveData<UIState<ArrayList<AdminPesananDetailModel>>> = _pesanan
    fun getUpdateKonfirmasiPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateKonfirmasiPembayaran
    fun getUpdateKonfirmasiSelesai(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateKonfirmasiSelesai
}