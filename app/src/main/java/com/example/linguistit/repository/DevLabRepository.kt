package com.example.linguistit.repository

import com.example.linguistit.model.SavedTerm
import com.example.linguistit.model.SprintResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DevLabRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val currentUid: String?
        get() = auth.currentUser?.uid

    fun saveSprintResult(sprintId: String, sprint: SprintResult, onResult: (Boolean) -> Unit) {
        val uid = currentUid ?: return onResult(false)

        firestore.collection("users")
            .document(uid)
            .collection("sprints")
            .document(sprintId)
            .set(sprint)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getSprintHistory(onResult: (List<SprintResult>?) -> Unit) {
        val uid = currentUid ?: return onResult(null)

        firestore.collection("users")
            .document(uid)
            .collection("sprints")
            .orderBy("date", Query.Direction.DESCENDING) // Orden cronológico inverso
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Mapeo automático de documentos NoSQL a una lista de objetos Kotlin
                val sprints = querySnapshot.toObjects(SprintResult::class.java)
                onResult(sprints)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun saveTechnicalTerm(term: SavedTerm, onResult: (Boolean) -> Unit) {
        val uid = currentUid ?: return onResult(false)

        firestore.collection("users")
            .document(uid)
            .collection("saved_terms")
            .add(term)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getSavedTerms(onResult: (List<SavedTerm>?) -> Unit) {
        val uid = currentUid ?: return onResult(null)

        firestore.collection("users")
            .document(uid)
            .collection("saved_terms")
            .orderBy("savedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val terms = querySnapshot.toObjects(SavedTerm::class.java)
                onResult(terms)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}