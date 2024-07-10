package com.example.aplikasipenjualanplafon.ui.activity.user.alamat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.AlamatModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihAlamatViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _alamatUser = MutableLiveData<UIState<ArrayList<AlamatModel>>>()
    val _updateMainAlamat = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _tambahAlamatUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _updateAlamatUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataAlamat(idUser:String){
        viewModelScope.launch(Dispatchers.IO) {
            _alamatUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.getAlamatUser("", idUser)
                _alamatUser.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _alamatUser.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postUpdateMainAlamat(
        idAlamat: String, idUser: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _updateMainAlamat.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.postUpdateMainAlamat(
                    "", idAlamat, idUser
                )
                _updateMainAlamat.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _updateMainAlamat.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postTambahAlamat(
        idUser: String, namaLengkap: String, nomorHp: String,
        alamat: String, detailAlamat: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _tambahAlamatUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.postTambahAlamat(
                    "", idUser, namaLengkap, nomorHp, alamat, detailAlamat
                )
                _tambahAlamatUser.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _tambahAlamatUser.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postUpdateAlamat(
        idAlamat: String, idUser: String, namaLengkap: String,
        nomorHp: String, alamat: String, detailAlamat: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _updateAlamatUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.postUpdateAlamat(
                    "", idAlamat, idUser, namaLengkap, nomorHp, alamat, detailAlamat
                )
                _updateAlamatUser.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _updateAlamatUser.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }



    fun getDataAlamat(): LiveData<UIState<ArrayList<AlamatModel>>> = _alamatUser
    fun getUpdateMainAlamat(): LiveData<UIState<ArrayList<ResponseModel>>> = _updateMainAlamat
    fun getTambahAlamat(): LiveData<UIState<ArrayList<ResponseModel>>> = _tambahAlamatUser
    fun getUpdateAlamat(): LiveData<UIState<ArrayList<ResponseModel>>> = _updateAlamatUser
}