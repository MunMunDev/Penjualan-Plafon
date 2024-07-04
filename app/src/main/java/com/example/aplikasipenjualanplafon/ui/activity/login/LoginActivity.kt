package com.example.aplikasipenjualanplafon.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.data.model.UsersModel
import com.example.aplikasipenjualanplafon.databinding.ActivityLoginBinding
import com.example.aplikasipenjualanplafon.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasipenjualanplafon.ui.activity.register.RegisterActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.main.MainActivity
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject
    lateinit var loading : LoadingAlertDialog
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        konfigurationUtils()
        button()
        setDataSebelumnya()
        getData()
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            val username = extras.getString("username")
            val password = extras.getString("password")

            loginBinding.etUsername.setText(username)
            loginBinding.etPassword.setText(password)
        }
    }

    private fun konfigurationUtils() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@LoginActivity)
    }

    private fun button(){
        btnDaftar()
        btnLogin()
    }

    private fun btnLogin() {
        loginBinding.apply {
            btnLogin.setOnClickListener{
                if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    loading.alertDialogLoading(this@LoginActivity)
                    cekUsers(etUsername.text.toString(), etPassword.text.toString())
                }
                else{
                    if(etUsername.text.isEmpty()){
                        etUsername.error = "Masukkan Username"
                    }
                    if(etPassword.text.isEmpty()){
                        etPassword.error = "Masukkan Password"
                    }
                }
            }
        }
    }

    private fun btnDaftar() {
        loginBinding.tvDaftar.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun cekUsers(username: String, password: String) {
        loginViewModel.fetchDataUser(username, password)
    }

    private fun getData(){
        loginViewModel.getDataUser().observe(this@LoginActivity){result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@LoginActivity)
                is UIState.Success-> setSuccessDataUser(result.data)
                is UIState.Failure -> setFailureDataUser(result.message)
            }
        }
    }

    private fun setFailureDataUser(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDataUser(data: ArrayList<UsersModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            successFetchLogin(data)
        } else{
            Toast.makeText(this@LoginActivity, "Data tidak ditemukan \nPastikan Username dan Password Benar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun successFetchLogin(userModel: ArrayList<UsersModel>){
        var valueIdUser = 0
        userModel[0].idUser?.let {
            valueIdUser = it.toInt()
        }
        val valueNama = userModel[0].nama.toString()
        val valueAlamat = userModel[0].alamat.toString()
        val valueNomorHp = userModel[0].nomorHp.toString()
        val valueUsername = userModel[0].username.toString()
        val valuePassword = userModel[0].password.toString()
        val valueSebagai= userModel[0].sebagai.toString()

        try{
            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
            sharedPreferencesLogin.setLogin(valueIdUser, valueNama, valueAlamat, valueNomorHp, valueUsername, valuePassword, valueSebagai)
            if(valueSebagai=="user"){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else{
                startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
            }
        } catch (ex: Exception){
            Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
        }
    }


    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}