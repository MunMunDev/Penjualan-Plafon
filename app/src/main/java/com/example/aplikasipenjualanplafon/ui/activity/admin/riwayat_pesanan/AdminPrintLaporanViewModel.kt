package com.example.aplikasipenjualanplafon.ui.activity.admin.riwayat_pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.AdminPesananDetailModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPrintLaporanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananValModel>>>()

    fun fetchPesanan(print: String){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminPrintLaporan("", print)
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }
    fun getPesanan(): LiveData<UIState<ArrayList<RiwayatPesananValModel>>> = _pesanan
}