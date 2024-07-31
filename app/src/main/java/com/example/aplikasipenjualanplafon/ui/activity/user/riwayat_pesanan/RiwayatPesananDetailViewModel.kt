package com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.data.model.TestimoniModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RiwayatPesananDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _fetchTestimoni = MutableLiveData<UIState<ArrayList<TestimoniModel>>>()
    val _detailRiwayatPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananValModel>>>()
    private val _postTambahTestimoni = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateTestimoni = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchTestimoni(
        idPemesanan: String, idPlafon:String
    ){
        viewModelScope.launch {
            _fetchTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTestimoniRiwayatPesanan(
                    "", idPemesanan, idPlafon
                )
                _fetchTestimoni.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _fetchTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun fetchDetailRiwayatPesanan(idPemesanan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailRiwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getDetailRiwayatPesanan("", idPemesanan)
                _detailRiwayatPesanan.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _detailRiwayatPesanan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }



    fun postTambahData(
        idPemesanan: String, idPlafon: String, testimoni: String, bintang: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.postTambahTestimoni("", idPemesanan, idPlafon, testimoni, bintang)
                _postTambahTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postTambahTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehData(
        id_testimoni:String, testimoni:String, bintang:String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.postUpdateTestimoni("", id_testimoni, testimoni, bintang)
                _postUpdateTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postUpdateTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTestimoni(): LiveData<UIState<ArrayList<TestimoniModel>>> = _fetchTestimoni
    fun getDetailRiwayatPesanan(): LiveData<UIState<ArrayList<RiwayatPesananValModel>>> = _detailRiwayatPesanan
    fun getResponseTambahTestimoni(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahTestimoni
    fun getResponseUpdateTestimoni(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateTestimoni

}