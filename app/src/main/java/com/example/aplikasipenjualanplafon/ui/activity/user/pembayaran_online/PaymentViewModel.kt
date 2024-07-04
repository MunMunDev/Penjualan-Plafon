package com.example.aplikasipenjualanplafon.ui.activity.user.pembayaran_online

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
class PaymentViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _pembayaran = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    val _responseRegistrasiPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataPembayaran(idUser:String){
        viewModelScope.launch(Dispatchers.IO) {
            _pembayaran.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getKeranjangBelanjaUser("", idUser)
                _pembayaran.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception){
                _pembayaran.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postRegistrasiPembayaran(id_pembayaran: String, idUser: String, keterangan:String){
        viewModelScope.launch(Dispatchers.IO){
            _responseRegistrasiPembayaran.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataRegistrasiPembayaran = api.postRegistrasiPembayaran(
                    "", id_pembayaran, idUser, keterangan
                )
                _responseRegistrasiPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseRegistrasiPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }

    fun getDataPembayaran(): LiveData<UIState<ArrayList<PesananModel>>> = _pembayaran
    fun getRegistrasiPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseRegistrasiPembayaran
}