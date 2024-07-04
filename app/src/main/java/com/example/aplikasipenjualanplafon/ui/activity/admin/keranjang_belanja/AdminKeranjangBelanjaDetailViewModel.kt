package com.example.aplikasipenjualanplafon.ui.activity.admin.keranjang_belanja

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
class AdminKeranjangBelanjaDetailViewModel  @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _detailRiwayatPembayaran = MutableLiveData<UIState<ArrayList<RiwayatPesananValModel>>>()

    fun fetchDetailRiwayatPembayaran(idUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailRiwayatPembayaran.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getAdminKeranjangBelanjaDetail("", idUser)
                _detailRiwayatPembayaran.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _detailRiwayatPembayaran.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getDetailRiwayatPembayaran(): LiveData<UIState<ArrayList<RiwayatPesananValModel>>> =
        _detailRiwayatPembayaran
}
