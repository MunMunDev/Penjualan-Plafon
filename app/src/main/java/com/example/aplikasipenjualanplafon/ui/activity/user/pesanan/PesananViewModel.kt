package com.example.aplikasipenjualanplafon.ui.activity.user.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananHalModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PesananViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _pesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananHalModel>>>()

    fun fetchPesanan(idUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getPesanan("", idUser)
                _pesanan.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _pesanan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<RiwayatPesananHalModel>>> = _pesanan
}