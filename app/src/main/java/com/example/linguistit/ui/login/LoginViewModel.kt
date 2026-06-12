package com.example.linguistit.ui.login

import AuthRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linguistit.model.AuthResult

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult> = _loginResult

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _loginResult.value = AuthResult.Error("Campos vacíos")
            return
        }

        repository.login(email, pass) { result ->
            _loginResult.postValue(result)
        }
    }
}