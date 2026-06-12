package com.example.linguistit.ui.home

import AuthRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.linguistit.databinding.FragmentProfileBinding
import com.example.linguistit.model.User

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val repository = AuthRepository()

    private var isEditing = false
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserData()

        binding.btnEditSave.setOnClickListener {
            if (isEditing) {
                saveUpdatedData()
            } else {
                toggleEditMode(true)
            }
        }
    }

    private fun loadUserData() {
        repository.getCurrentUserProfile { user ->
            if (isAdded && _binding != null && user != null) {
                currentUser = user
                displayUserData(user)
            }
        }
    }

    private fun displayUserData(user: User) {
        binding.tvName.text = "${user.nombre} ${user.apellido}"
        binding.tvAge.text = "${user.edad} años"
        binding.tvCourse.text = user.curso
        binding.tvEmail.text = user.email
    }

    private fun toggleEditMode(enEdicion: Boolean) {
        isEditing = enEdicion

        if (enEdicion) {
            binding.etName.visibility = View.VISIBLE
            binding.etAge.visibility = View.VISIBLE
            binding.etCourse.visibility = View.VISIBLE

            binding.tvName.visibility = View.GONE
            binding.tvAge.visibility = View.GONE
            binding.tvCourse.visibility = View.GONE

            binding.etName.setText(currentUser?.nombre)
            binding.etAge.setText(currentUser?.edad?.toString())
            binding.etCourse.setText(currentUser?.curso)

            binding.btnEditSave.text = "Guardar Cambios"
        } else {
            binding.etName.visibility = View.GONE
            binding.etAge.visibility = View.GONE
            binding.etCourse.visibility = View.GONE

            binding.tvName.visibility = View.VISIBLE
            binding.tvAge.visibility = View.VISIBLE
            binding.tvCourse.visibility = View.VISIBLE

            binding.btnEditSave.text = "Editar Perfil"
        }
    }

    private fun saveUpdatedData() {
        val nuevoNombre = binding.etName.text.toString().trim()
        val nuevaEdadStr = binding.etAge.text.toString().trim()
        val nuevoCurso = binding.etCourse.text.toString().trim()

        if (nuevoNombre.isBlank() || nuevaEdadStr.isBlank() || nuevoCurso.isBlank()) {
            Toast.makeText(requireContext(), "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaEdad = nuevaEdadStr.toIntOrNull() ?: 0

        val usuarioActualizado = User(
            nombre = nuevoNombre,
            apellido = currentUser?.apellido ?: "",
            email = currentUser?.email ?: "",
            edad = nuevaEdad,
            curso = nuevoCurso
        )

        binding.btnEditSave.isEnabled = false

        repository.updateUserProfile(usuarioActualizado) { exito ->
            if (isAdded && _binding != null) {
                binding.btnEditSave.isEnabled = true
                if (exito) {
                    Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    currentUser = usuarioActualizado
                    displayUserData(usuarioActualizado)
                    toggleEditMode(false)
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}