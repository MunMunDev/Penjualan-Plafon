package com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan

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
class RiwayatPesananViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _riwayatPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananHalModel>>>()

    fun fetchRiwayatPesanan(idUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _riwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getRiwayatPesanan("", idUser)
                _riwayatPesanan.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _riwayatPesanan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getRiwayatPesanan(): LiveData<UIState<ArrayList<RiwayatPesananHalModel>>> = _riwayatPesanan
}