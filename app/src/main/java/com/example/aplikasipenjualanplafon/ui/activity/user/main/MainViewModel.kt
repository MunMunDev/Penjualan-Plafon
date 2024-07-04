package com.example.aplikasipenjualanplafon.ui.activity.user.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    private var _postUpdatePesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postHapusPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postPesan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchPesanan(idUser:String){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getKeranjangBelanjaUser("", idUser)
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpadatePesanan(idPesanan:String, jumlah:String ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdatePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.postUpdatePesanan("", idPesanan, jumlah)
                _postUpdatePesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _postUpdatePesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postHapusPesanan(idPesanan:String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.postHapusPesanan("", idPesanan)
                _postHapusPesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _postHapusPesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postPesan(idUser:String, alamat:String, metode_pembayaran: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postPesan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.postPesan("", idUser, alamat, metode_pembayaran)
                _postPesan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _postPesan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<PesananModel>>> = _pesanan
    fun getPostUpdatePesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdatePesanan
    fun getPostHapusPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusPesanan
    fun getPostPesan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postPesan

}