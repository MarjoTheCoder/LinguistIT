package com.example.linguistit.ui.password

import AuthRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linguistit.model.AuthResult

class RestorePasswordViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _restoreResult = MutableLiveData<AuthResult>()
    val restoreResult: LiveData<AuthResult> = _restoreResult

    fun sendResetEmail(email: String) {
        if (email.isBlank()) {
            _restoreResult.value = AuthResult.Error("Por favor, ingresa tu correo")
            return
        }

        repository.sendPasswordReset(email) { result ->
            _restoreResult.postValue(result)
        }
    }
}