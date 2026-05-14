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
import com.example.linguistit.databinding.FragmentRegisterInformationBinding
import com.example.linguistit.model.AuthResult

class RegisterInformationFragment : Fragment() {

    private var _binding: FragmentRegisterInformationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEventListeners()
        observeViewModel()
    }

    private fun setupEventListeners() {
        binding.btnFinishRegister.setOnClickListener {
            val nombre = binding.etName.text.toString()
            val apellido = binding.etSurname.text.toString()
            val edadString = binding.etAge.text.toString()

            if (validateFields(nombre, apellido, edadString)) {
                viewModel.completeRegistration(
                    nombre,
                    apellido,
                    edadString.toInt()
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    binding.btnFinishRegister.isEnabled = false
                }
                is AuthResult.Success -> {
                    binding.btnFinishRegister.isEnabled = true
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is AuthResult.Error -> {
                    binding.btnFinishRegister.isEnabled = true
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateFields(nom: String, ape: String, edad: String): Boolean {
        if (nom.isBlank() || ape.isBlank() || edad.isBlank()) {
            Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}