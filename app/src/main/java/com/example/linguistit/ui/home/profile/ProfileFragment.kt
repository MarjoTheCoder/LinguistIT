package com.example.linguistit.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.linguistit.MainActivity
import com.example.linguistit.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón de acción configurado con su texto correspondiente
        binding.btnEditProfile.text = "Editar Perfil"

        // Escuchamos si el diálogo actualiza los datos en Firestore
        parentFragmentManager.setFragmentResultListener("profile_updated", viewLifecycleOwner) { _, _ ->
            checkUserSession()
        }

        checkUserSession()
    }

    private fun checkUserSession() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val email = currentUser.email ?: "Sin correo"

            fetchUserProfile(uid, email)
            setupButtonAction()
        } else {
            Toast.makeText(requireContext(), "No se encontró sesión activa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserProfile(uid: String, email: String) {
        (activity as? MainActivity)?.showLoading(true)

        firestore.collection("usuarios").document(uid)
            .get()
            .addOnSuccessListener { document ->
                (activity as? MainActivity)?.showLoading(false)

                if (document != null && document.exists()) {
                    val nombre = document.getString("nombre") ?: ""
                    val apellido = document.getString("apellido") ?: ""
                    val edad = document.getLong("edad") ?: 0L
                    val curso = document.getString("curso") ?: "No asignado"

                    updateUI(nombre, apellido, edad, curso, email)
                } else {
                    updateUI("Usuario", "Nuevo", 0L, "No asignado", email)
                }
            }
            .addOnFailureListener { exception ->
                (activity as? MainActivity)?.showLoading(false)
                Toast.makeText(requireContext(), "Error al cargar perfil: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateUI(nombre: String, apellido: String, edad: Long, curso: String, email: String) {
        binding.apply {
            tvProfileName.text = "$nombre $apellido"
            tvProfileEmail.text = email
            tvProfileAge.text = if (edad > 0) "$edad años" else "Edad no especificada"

            // CORREGIDO: IDs exactos de tu fragment_profile.xml
            tvProfileRole.text = curso
            tvProfileInitials.text = getInitials(nombre, apellido)
        }
    }

    private fun getInitials(nombre: String, apellido: String): String {
        val firstInitial = nombre.trim().firstOrNull()?.uppercase() ?: ""
        val secondInitial = apellido.trim().firstOrNull()?.uppercase() ?: ""
        return if (firstInitial.isEmpty() && secondInitial.isEmpty()) "?" else "$firstInitial$secondInitial"
    }

    private fun setupButtonAction() {
        binding.btnEditProfile.setOnClickListener {
            // Levanta el diálogo flotante de edición
            RegisterProfileDialog().show(childFragmentManager, "RegisterProfile")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}