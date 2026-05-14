package com.example.linguistit.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linguistit.R
import com.example.linguistit.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNextRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val pass = binding.etRegisterPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()

            if (validateFirstStep(email, pass, confirmPass)) {
                viewModel.emailTemp = email
                viewModel.passwordTemp = pass

                findNavController().navigate(R.id.action_registerFragment_to_registerInformationFragment)
            }
        }
    }

    private fun validateFirstStep(email: String, pass: String, confirm: String): Boolean {
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(requireContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass != confirm) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass.length < 6) {
            Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}