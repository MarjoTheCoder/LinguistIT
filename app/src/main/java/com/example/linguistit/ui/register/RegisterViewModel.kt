package com.example.linguistit.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linguistit.data.AuthRepository
import com.example.linguistit.model.AuthResult
import com.example.linguistit.model.User

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository()

    var emailTemp: String = ""
    var passwordTemp: String = ""

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult> = _registerResult

    fun completeRegistration(nombre: String, apellido: String, edad: Int) {
        val newUser = User(
            nombre = nombre,
            apellido = apellido,
            email = emailTemp,
            edad = edad
        )

        repository.registerUser(newUser, passwordTemp) { result ->
            _registerResult.postValue(result)
        }
    }
}