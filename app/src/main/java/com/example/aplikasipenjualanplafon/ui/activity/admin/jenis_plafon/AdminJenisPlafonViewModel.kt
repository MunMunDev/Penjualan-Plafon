package com.example.aplikasipenjualanplafon.ui.activity.admin.jenis_plafon

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
class AdminJenisPlafonViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private var _jenisPlafon = MutableLiveData<UIState<ArrayList<JenisPlafonModel>>>()
    private var _postTambahJenisPlafon = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdateJenisPlafon = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
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

    fun postTambahPlafon(jenisPlafon: String, keunggulan: String){
        viewModelScope.launch {
            _postTambahJenisPlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postTambahJenisPlafon(
                    "", jenisPlafon, keunggulan
                )
                _postTambahJenisPlafon.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postTambahJenisPlafon.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePlafon(
        idJenisPlafon: String, jenisPlafon: String, keunggulan: String
    ){
        viewModelScope.launch {
            _postUpdateJenisPlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postUpdateJenisPlafon(
                    "", idJenisPlafon, jenisPlafon, keunggulan
                )
                _postUpdateJenisPlafon.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postUpdateJenisPlafon.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeletePlafon(idJenisPlafon: String){
        viewModelScope.launch {
            _postDeletePlafon.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postDeleteJenisPlafon(
                    "", idJenisPlafon
                )
                _postDeletePlafon.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postDeletePlafon.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getJenisPlafon(): LiveData<UIState<ArrayList<JenisPlafonModel>>> = _jenisPlafon
    fun getTambahPlafon(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahJenisPlafon
    fun getUpdatePlafon(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateJenisPlafon
    fun getDeletePlafon(): LiveData<UIState<ArrayList<ResponseModel>>> = _postDeletePlafon
}