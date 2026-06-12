package com.example.linguistit.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linguistit.R
import com.example.linguistit.adapter.ProjectAdapter
import com.example.linguistit.data.repository.ProjectRepository
import com.example.linguistit.model.Project
import com.google.firebase.auth.FirebaseAuth

class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private val projectRepository = ProjectRepository()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var projectAdapter: ProjectAdapter
    private val currentUserId by lazy { auth.currentUser?.uid ?: "USER_BACKUP_ID_SESSION" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enlace de componentes UI
        val etName = view.findViewById<EditText>(R.id.etProjectName)
        val etDesc = view.findViewById<EditText>(R.id.etProjectDesc)
        val actvStatus = view.findViewById<AutoCompleteTextView>(R.id.actvStatus)
        val etProgress = view.findViewById<EditText>(R.id.etProjectProgress)
        val btnSave = view.findViewById<Button>(R.id.btnSaveProject)
        val rvProjects = view.findViewById<RecyclerView>(R.id.rvProjects)

        val listaEstatusDisponibles = arrayOf("Backlog", "En Desarrollo", "QA", "Completado", "Bloqueado")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listaEstatusDisponibles)
        actvStatus.setAdapter(spinnerAdapter)

        projectAdapter = ProjectAdapter(emptyList()) { proyectoSeleccionado ->
            Toast.makeText(requireContext(), "Proyecto: ${proyectoSeleccionado.name} se encuentra en ${proyectoSeleccionado.status}", Toast.LENGTH_SHORT).show()
        }
        rvProjects.layoutManager = LinearLayoutManager(requireContext())
        rvProjects.adapter = projectAdapter

        consultarEcosistemaProyectos()

        btnSave.setOnClickListener {
            val nombre = etName.text.toString().trim()
            val descripcion = etDesc.text.toString().trim()
            val estatus = actvStatus.text.toString().trim()
            val progresoStr = etProgress.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty() || estatus.isEmpty() || progresoStr.isEmpty()) {
                Toast.makeText(requireContext(), "Completa el alcance completo del proyecto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progreso = progresoStr.toIntOrNull() ?: 0

            val nuevoProyecto = Project(
                name = nombre,
                description = descripcion,
                status = estatus,
                progress = progreso.coerceIn(0, 100)
            )

            btnSave.isEnabled = false
            projectRepository.guardarNuevoProyecto(
                userId = currentUserId,
                proyecto = nuevoProyecto,
                onSuccess = {
                    btnSave.isEnabled = true
                    Toast.makeText(requireContext(), "Proyecto inicializado correctamente", Toast.LENGTH_SHORT).show()

                    // Limpieza del buffer del formulario
                    etName.text.clear()
                    etDesc.text.clear()
                    actvStatus.setText("", false)
                    etProgress.text.clear()

                    consultarEcosistemaProyectos()
                },
                onFailure = { exception ->
                    btnSave.isEnabled = true
                    Toast.makeText(requireContext(), "Fallo de sincronización: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun consultarEcosistemaProyectos() {
        projectRepository.cargarProyectos(
            userId = currentUserId,
            onSuccess = { proyectosRecuperados ->
                val listaProcesada = proyectosRecuperados.sortedByDescending { it.progress }
                projectAdapter.updateData(listaProcesada)
            },
            onFailure = {
                Toast.makeText(requireContext(), "Error al mapear la mesa de trabajo remota", Toast.LENGTH_SHORT).show()
            }
        )
    }
}