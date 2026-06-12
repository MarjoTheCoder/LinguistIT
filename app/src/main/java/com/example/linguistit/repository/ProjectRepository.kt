package com.example.linguistit.data.repository

import com.example.linguistit.model.Project
import com.google.firebase.firestore.FirebaseFirestore

class ProjectRepository {
    private val db = FirebaseFirestore.getInstance()

    fun guardarNuevoProyecto(userId: String, proyecto: Project, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val documentRef = db.collection("users").document(userId).collection("projects").document()
        val proyectoFinal = proyecto.copy(id = documentRef.id)

        documentRef.set(proyectoFinal)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun cargarProyectos(userId: String, onSuccess: (List<Project>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users").document(userId).collection("projects")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val listaProyectos = querySnapshot.toObjects(Project::class.java)
                onSuccess(listaProyectos)
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun actualizarEstatusProyecto(userId: String, projectId: String, nuevoEstatus: String, nuevoProgreso: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users").document(userId).collection("projects").document(projectId)
            .update(mapOf(
                "status" to nuevoEstatus,
                "progress" to nuevoProgreso
            ))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}