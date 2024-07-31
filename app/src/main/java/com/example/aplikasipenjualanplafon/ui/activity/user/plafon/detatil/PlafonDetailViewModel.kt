package com.example.aplikasipenjualanplafon.ui.activity.user.plafon.detatil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.TestimoniModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlafonDetailViewModel @Inject constructor(
    private var api: ApiService
): ViewModel() {
    private var _fetchTestimoni = MutableLiveData<UIState<ArrayList<TestimoniModel>>>()
    private var _postTambahPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    fun fetchTestimoni(
        idPlafon: String
    ){
        viewModelScope.launch {
            _fetchTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTestimoni(
                    "", idPlafon
                )
                _fetchTestimoni.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _fetchTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postTambahPesanan(
        idPlafon: String, idUser: String, jumlah: String
    ){
        viewModelScope.launch {
            _postTambahPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPlafon = api.postTambahPesanan(
                    "", idPlafon, idUser, jumlah
                )
                _postTambahPesanan.postValue(UIState.Success(postTambahPlafon))
            } catch (ex: Exception){
                _postTambahPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTestimoni(): LiveData<UIState<ArrayList<TestimoniModel>>> = _fetchTestimoni
    fun getTambahPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahPesanan
}