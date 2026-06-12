package com.example.linguistit.data

import com.example.linguistit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getCurrentUserData(onResult: (User?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(null)
            return
        }

        db.collection("usuarios").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = User(
                        id = 0,
                        nombre = document.getString("nombre") ?: "",
                        apellido = document.getString("apellido") ?: "",
                        email = document.getString("email") ?: "",
                        edad = document.getLong("edad")?.toInt() ?: 0,
                        curso = document.getString("curso") ?: ""
                    )
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateUserData(user: User, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        val userUpdate = hashMapOf(
            "nombre" to user.nombre,
            "apellido" to user.apellido,
            "edad" to user.edad,
            "curso" to user.curso
        )

        db.collection("usuarios").document(userId)
            .update(userUpdate as Map<String, Any>)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}