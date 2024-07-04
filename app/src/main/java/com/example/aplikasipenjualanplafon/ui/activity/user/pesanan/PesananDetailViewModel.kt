package com.example.aplikasipenjualanplafon.ui.activity.user.pesanan

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
class PesananDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _detailPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananValModel>>>()

    fun fetchDetailRiwayatPembayaran(idPemesanan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getPesananDetail("", idPemesanan)
                _detailPesanan.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _detailPesanan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getDetailRiwayatPembayaran(): LiveData<UIState<ArrayList<RiwayatPesananValModel>>> = _detailPesanan

}