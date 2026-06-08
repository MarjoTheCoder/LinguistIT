package com.example.linguistit.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.linguistit.MainActivity
import com.example.linguistit.databinding.FragmentRegisterInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterInformationFragment : Fragment() {

    private var _binding: FragmentRegisterInformationBinding? = null
    private val binding get() = _binding!!

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, viewMetadata: Bundle?) {
        super.onViewCreated(view, viewMetadata)

        binding.btnFinishRegister.setOnClickListener {
            if (validateFields()) {
                saveUserInformation()
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        binding.apply {
            if (etName.text.toString().trim().isEmpty()) {
                tilName.error = "El nombre es obligatorio"
                isValid = false
            } else {
                tilName.error = null
            }

            if (etSurname.text.toString().trim().isEmpty()) {
                tilSurname.error = "El apellido es obligatorio"
                isValid = false
            } else {
                tilSurname.error = null
            }

            if (etAge.text.toString().trim().isEmpty()) {
                tilAge.error = "Por favor, ingresa tu edad"
                isValid = false
            } else {
                tilAge.error = null
            }
        }

        return isValid
    }

    private fun saveUserInformation() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Error: No se encontró una sesión activa", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnFinishRegister.isEnabled = false

        val uid = currentUser.uid
        val name = binding.etName.text.toString().trim()
        val surname = binding.etSurname.text.toString().trim()
        val age = binding.etAge.text.toString().trim().toLongOrNull() ?: 0L

        val userMap = hashMapOf(
            "nombre" to name,
            "apellido" to surname,
            "edad" to age,
            "curso" to "No asignado"
        )

        firestore.collection("usuarios").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "¡Registro completado con éxito!", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
            .addOnFailureListener { exception ->
                binding.btnFinishRegister.isEnabled = true
                Toast.makeText(requireContext(), "Error al guardar: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}