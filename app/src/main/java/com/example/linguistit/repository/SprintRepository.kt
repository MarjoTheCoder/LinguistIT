package com.example.linguistit.repository

import com.example.linguistit.model.Sprint
import com.google.firebase.firestore.FirebaseFirestore

class SprintRepository {
    private val db = FirebaseFirestore.getInstance()

    fun cargarHistorialSprints(
        userId: String,
        onSuccess: (List<Sprint>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("sprints")
            .get()
            .addOnSuccessListener { documents ->
                val listaSprints = mutableListOf<Sprint>()
                for (document in documents) {
                    val sprint = document.toObject(Sprint::class.java)
                    listaSprints.add(sprint)
                }
                onSuccess(listaSprints)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun guardarNuevoSprint(
        userId: String,
        nuevoSprint: Sprint,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("sprints")
            .add(nuevoSprint)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}