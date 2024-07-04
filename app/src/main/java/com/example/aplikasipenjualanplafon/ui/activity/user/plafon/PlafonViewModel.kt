package com.example.aplikasipenjualanplafon.ui.activity.user.plafon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlafonViewModel @Inject constructor(
   private val api: ApiService
): ViewModel() {
   private var _plafon = MutableLiveData<UIState<ArrayList<PlafonModel>>>()
   private var _postTambahPesanan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

   fun fetchPlafon(){
      viewModelScope.launch(Dispatchers.IO) {
         _plafon.postValue(UIState.Loading)
         delay(1_000)
         try {
            val fetchPlafon = api.getAllPlafon("")
            _plafon.postValue(UIState.Success(fetchPlafon))
         } catch (ex: Exception){
            _plafon.postValue(UIState.Failure("Gagal : ${ex.message}"))
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

   fun getPlafon(): LiveData<UIState<ArrayList<PlafonModel>>> = _plafon
   fun getTambahPesanan(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahPesanan
}