package com.example.linguistit.ui.home.devlab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.example.linguistit.repository.SprintRepository
import com.example.linguistit.model.Sprint
import com.example.linguistit.R
import com.example.linguistit.adapter.SprintAdapter

class DevLabFragment : Fragment(R.layout.fragment_dev_lab) {

    private val sprintRepository = SprintRepository()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var sprintAdapter: SprintAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTechnicalTopic = view.findViewById<EditText>(R.id.etTechnicalTopic)
        val etScore = view.findViewById<EditText>(R.id.etScore)
        val etUserAnswer = view.findViewById<EditText>(R.id.etUserAnswer)
        val btnGuardarSprint = view.findViewById<Button>(R.id.btnGuardarSprint)
        val rvSprints = view.findViewById<RecyclerView>(R.id.rvSprints)

        sprintAdapter = SprintAdapter()
        rvSprints.layoutManager = LinearLayoutManager(requireContext())
        rvSprints.adapter = sprintAdapter

        val currentUserId = auth.currentUser?.uid ?: "EILiQwPpZXVwOHAvSb2..."

        cargarHistorial(currentUserId)

        btnGuardarSprint.setOnClickListener {
            val tema = etTechnicalTopic.text.toString().trim()
            val scoreStr = etScore.text.toString().trim()
            val respuesta = etUserAnswer.text.toString().trim()

            if (tema.isEmpty() || scoreStr.isEmpty() || respuesta.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val score = scoreStr.toIntOrNull() ?: 0
            val pasoElSprint = score >= 70

            val nuevoSprint = Sprint(
                date = Timestamp.now(),
                passed = pasoElSprint,
                score = score,
                technicalTopic = tema,
                type = "Práctica Manual",
                userAnswer = respuesta
            )

            sprintRepository.guardarNuevoSprint(
                userId = currentUserId,
                nuevoSprint = nuevoSprint,
                onSuccess = {
                    Toast.makeText(requireContext(), "¡Sprint guardado con éxito!", Toast.LENGTH_SHORT).show()

                    etTechnicalTopic.text.clear()
                    etScore.text.clear()
                    etUserAnswer.text.clear()

                    cargarHistorial(currentUserId)
                },
                onFailure = { error ->
                    Toast.makeText(requireContext(), "Error al guardar: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun cargarHistorial(userId: String) {
        sprintRepository.cargarHistorialSprints(
            userId = userId,
            onSuccess = { listaDeSprints ->
                val listaOrdenada = listaDeSprints.sortedByDescending { it.date }
                sprintAdapter.updateData(listaOrdenada)
            },
            onFailure = { error ->
                Log.e("DevLabError", "Error al actualizar historial", error)
            }
        )
    }
}