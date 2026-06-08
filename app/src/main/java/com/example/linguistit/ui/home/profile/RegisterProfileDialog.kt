package com.example.linguistit.ui.home.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.linguistit.MainActivity
import com.example.linguistit.databinding.DialogRegisterProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterProfileDialog : DialogFragment() {

    private var _binding: DialogRegisterProfileBinding? = null
    private val binding get() = _binding!!

    // Instancias de Firebase
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val uid: String? by lazy { FirebaseAuth.getInstance().currentUser?.uid }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogRegisterProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Configura el diálogo para que sea flotante, estético y responsivo
        dialog?.window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        loadCurrentDataOfUser()
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveDataToFirestore()
            }
        }
    }

    // Validación local para pintar errores en rojo si falta llenar algún campo
    private fun validateFields(): Boolean {
        var isValid = true
        binding.apply {
            if (etNombre.text.toString().trim().isEmpty()) {
                tilNombre.error = "El nombre es obligatorio"
                isValid = false
            } else { tilNombre.error = null }

            if (etApellido.text.toString().trim().isEmpty()) {
                tilApellido.error = "El apellido es obligatorio"
                isValid = false
            } else { tilApellido.error = null }

            if (etEdad.text.toString().trim().isEmpty()) {
                tilEdad.error = "Ingresa tu edad"
                isValid = false
            } else { tilEdad.error = null }

            if (etCurso.text.toString().trim().isEmpty()) {
                tilCurso.error = "Especifica tu curso o especialidad"
                isValid = false
            } else { tilCurso.error = null }
        }
        return isValid
    }

    // OPCIONAL pero recomendado: Carga los datos actuales en los campos si el usuario ya los tenía registrados
    private fun loadCurrentDataOfUser() {
        val userUid = uid ?: return

        firestore.collection("usuarios").document(userUid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    binding.apply {
                        etNombre.setText(document.getString("nombre"))
                        etApellido.setText(document.getString("apellido"))
                        etEdad.setText(document.getLong("edad")?.toString() ?: "")
                        etCurso.setText(document.getString("curso"))
                    }
                }
            }
    }

    // El núcleo de la persistencia en Firestore
    private fun saveDataToFirestore() {
        val userUid = uid ?: return

        // Encendemos el loader global de tu MainActivity
        (activity as? MainActivity)?.showLoading(true)

        // Mapeo con las llaves exactas que lee el ProfileFragment
        val userMap = hashMapOf(
            "nombre" to binding.etNombre.text.toString().trim(),
            "apellido" to binding.etApellido.text.toString().trim(),
            "edad" to binding.etEdad.text.toString().trim().toLong(),
            "curso" to binding.etCurso.text.toString().trim()
        )

        // Guarda o actualiza el documento con el UID del usuario
        firestore.collection("usuarios").document(userUid)
            .set(userMap)
            .addOnSuccessListener {
                (activity as? MainActivity)?.showLoading(false)
                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()

                // NOTIFICACIÓN: Gatillo reactivo que le avisa al ProfileFragment que debe redibujarse
                parentFragmentManager.setFragmentResult("profile_updated", bundleOf("success" to true))

                dismiss() // Cierra el cuadro de diálogo
            }
            .addOnFailureListener { e ->
                (activity as? MainActivity)?.showLoading(false)
                Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}