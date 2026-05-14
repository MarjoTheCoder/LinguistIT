package com.example.linguistit.data

import com.example.linguistit.model.AuthResult
import com.example.linguistit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun login(email: String, pass: String, callback: (AuthResult) -> Unit) {
        callback(AuthResult.Loading)

        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(AuthResult.Success)
                } else {
                    callback(AuthResult.Error(task.exception?.message ?: "Error al iniciar sesión"))
                }
            }
    }

    fun registerUser(user: User, pass: String, callback: (AuthResult) -> Unit) {
        callback(AuthResult.Loading)

        firebaseAuth.createUserWithEmailAndPassword(user.email, pass)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    saveUserToFirestore(user, callback)
                } else {
                    callback(AuthResult.Error(authTask.exception?.message ?: "Error en el registro"))
                }
            }
    }

    private fun saveUserToFirestore(user: User, callback: (AuthResult) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid ?: return

        val userMap = hashMapOf(
            "nombre" to user.nombre,
            "apellido" to user.apellido,
            "email" to user.email,
            "edad" to user.edad
        )

        firestore.collection("alumnos").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                callback(AuthResult.Success)
            }
            .addOnFailureListener { e ->
                callback(AuthResult.Error(e.message ?: "Error al guardar datos de perfil"))
            }
    }


    fun sendPasswordReset(email: String, callback: (AuthResult) -> Unit) {
        callback(AuthResult.Loading)

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(AuthResult.Success)
                } else {
                    callback(AuthResult.Error(task.exception?.message ?: "Error al enviar correo"))
                }
            }
    }
}