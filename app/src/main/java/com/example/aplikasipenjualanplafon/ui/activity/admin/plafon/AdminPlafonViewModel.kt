package com.example.aplikasipenjualanplafon.ui.activity.admin.plafon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminPlafonViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _jenisPlafon = MutableLiveData<UIState<ArrayList<JenisPlafonModel>>>()
    private var _plafon = MutableLiveData<UIState<ArrayList<PlafonModel>>>()
    private var _postTambahPlafon = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdatePlafon = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdatePlafonNoImage = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postDeletePlafon = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchJenisPlafon(){
        viewModelScope.launch(Dispatchers.IO) {
            _jenisPlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchJenisPlafon = api.getJenisPlafon("")
                _jenisPlafon.postValue(UIState.Success(fetchJenisPlafon))
            } catch (ex: Exception){
                _jenisPlafon.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchPlafon(){
        viewModelScope.launch(Dispatchers.IO) {
            _plafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPlafon = api.getAllPlafon("")
                _plafon.postValue(UIState.Success(fetchPlafon))
            } catch (ex: Exception){
                _plafon.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahPlafon(
        tambahPlafon:RequestBody, idJenisPlafon: RequestBody, keterangan: RequestBody,
        gambar: MultipartBody.Part, stok:RequestBody, hargaPermeter:RequestBody
    ){
        viewModelScope.launch {
            _postTambahPlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postTambahPlafon(
                    tambahPlafon, idJenisPlafon, keterangan, gambar, stok, hargaPermeter
                )
                _postTambahPlafon.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postTambahPlafon.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePlafon(
        updatePlafon:RequestBody, idPlafon: RequestBody, idJenisPlafon: RequestBody, keterangan: RequestBody,
        gambar: MultipartBody.Part, stok: RequestBody, hargaPermeter:RequestBody
    ){
        viewModelScope.launch {
            _postUpdatePlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postUpdatePlafon(
                    updatePlafon, idPlafon, idJenisPlafon, keterangan, gambar, stok, hargaPermeter
                )
                _postUpdatePlafon.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postUpdatePlafon.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePlafonNoImage(
        updatePlafon:String, idPlafon: String, idJenisPlafon: String, keterangan: String, stok: String, hargaPermeter:String
    ){
        viewModelScope.launch {
            _postUpdatePlafonNoImage.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postUpdatePlafonNoImage(
                    updatePlafon, idPlafon, idJenisPlafon, keterangan, stok, hargaPermeter
                )
                _postUpdatePlafonNoImage.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postUpdatePlafonNoImage.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeletePlafon(idPlafon: String){
        viewModelScope.launch {
            _postDeletePlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postDeletePlafon(
                    "", idPlafon
                )
                _postDeletePlafon.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postDeletePlafon.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getJenisPlafon(): LiveData<UIState<ArrayList<JenisPlafonModel>>> = _jenisPlafon
    fun getPlafon(): LiveData<UIState<ArrayList<PlafonModel>>> = _plafon
    fun getTambahPlafon(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahPlafon
    fun getUpdatePlafon(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdatePlafon
    fun getUpdatePlafonNoImage(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdatePlafonNoImage
    fun getDeletePlafon(): LiveData<UIState<ArrayList<ResponseModel>>> = _postDeletePlafon
}