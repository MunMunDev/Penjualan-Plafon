package com.example.aplikasipenjualanplafon.ui.activity.user.riwayat_pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiwayatPesananDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _detailRiwayatPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananValModel>>>()

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

    fun getDetailRiwayatPesanan(): LiveData<UIState<ArrayList<RiwayatPesananValModel>>> = _detailRiwayatPesanan

}