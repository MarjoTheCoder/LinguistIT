package com.example.linguistit.ui.home.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.linguistit.MainActivity
import com.example.linguistit.databinding.FragmentSettingsBinding
import com.example.linguistit.model.SettingsItem
import com.example.linguistit.model.SettingsType
import com.example.linguistit.R

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("linguistit_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, viewMetadata: Bundle?) {
        super.onViewCreated(view, viewMetadata)
        setupSettingsList()
    }

    private fun setupSettingsList() {
        val isDarkMode = sharedPreferences.getBoolean("pref_dark_mode", false)
        val isNotificationsEnabled = sharedPreferences.getBoolean("pref_notifications", true)
        val isPrivacyEnabled = sharedPreferences.getBoolean("pref_privacy", true)
        val isAccessibilityEnabled = sharedPreferences.getBoolean("pref_accessibility", false)

        val settings = listOf(
            SettingsItem(1, "Modo Oscuro", "Cambiar el tema visual de la aplicación", android.R.drawable.ic_menu_compass, SettingsType.SWITCH, isDarkMode),
            SettingsItem(2, "Preferencias de Notificaciones", "Recibir avisos de estatus de tus proyectos", R.drawable.ic_popup_remainder, SettingsType.SWITCH, isNotificationsEnabled),
            SettingsItem(3, "Ajustes de Privacidad", "Controlar la recopilación de logs y analíticas", android.R.drawable.ic_lock_lock, SettingsType.SWITCH, isPrivacyEnabled),
            SettingsItem(4, "Accesibilidad", "Optimizar el tamaño de fuentes y contrastes", android.R.drawable.ic_menu_search, SettingsType.SWITCH, isAccessibilityEnabled)
        )

        val adapter = SettingsAdapter(settings) { item, isChecked ->
            handleSettingChange(item, isChecked)
        }

        binding.rvSettings.adapter = adapter
    }

    private fun handleSettingChange(item: SettingsItem, isChecked: Boolean) {
        val editor = sharedPreferences.edit()

        when (item.id) {
            1 -> {
                editor.putBoolean("pref_dark_mode", isChecked).apply()
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            2 -> {
                editor.putBoolean("pref_notifications", isChecked).apply()
                Toast.makeText(requireContext(), "Notificaciones: ${if (isChecked) "Activadas" else "Desactivadas"}", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                editor.putBoolean("pref_privacy", isChecked).apply()
                Toast.makeText(requireContext(), "Ajustes de privacidad actualizados", Toast.LENGTH_SHORT).show()
            }
            4 -> {
                editor.putBoolean("pref_accessibility", isChecked).apply()
                Toast.makeText(requireContext(), "Filtros de accesibilidad configurados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}