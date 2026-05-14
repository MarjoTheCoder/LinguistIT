package com.example.linguistit.model

data class User(
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val email: String,
    val edad: Int
)