package com.example.linguistit.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.linguistit.databinding.FragmentRestorePasswordBinding
import com.example.linguistit.model.AuthResult

class RestorePasswordFragment : Fragment() {

    private var _binding: FragmentRestorePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RestorePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEventListeners()
        observeViewModel()
    }

    private fun setupEventListeners() {
        // Botón Enviar Enlace
        binding.btnSendRestore.setOnClickListener {
            val email = binding.etRestoreEmail.text.toString()
            viewModel.sendResetEmail(email)
        }

        binding.btnBackToLoginFromRestore.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.restoreResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    binding.btnSendRestore.isEnabled = false
                }
                is AuthResult.Success -> {
                    binding.btnSendRestore.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Correo de recuperación enviado. Revisa tu bandeja de entrada.",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                }
                is AuthResult.Error -> {
                    binding.btnSendRestore.isEnabled = true
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}