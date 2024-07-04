package com.example.aplikasipenjualanplafon.ui.activity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasipenjualanplafon.data.database.api.ApiService
import com.example.aplikasipenjualanplafon.data.model.UsersModel
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _user = MutableLiveData<UIState<ArrayList<UsersModel>>>()

    fun fetchDataUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getUser("", username, password)
                _user.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _user.postValue(UIState.Failure("Error bang! ${ex.message}"))
            }
        }
    }

    fun getDataUser(): LiveData<UIState<ArrayList<UsersModel>>> = _user
}