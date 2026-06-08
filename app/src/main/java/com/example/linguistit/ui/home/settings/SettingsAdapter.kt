package com.example.linguistit.ui.home.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linguistit.databinding.ItemSettingBinding
import com.example.linguistit.model.SettingsItem
import com.example.linguistit.model.SettingsType

class SettingsAdapter(
    private val items: List<SettingsItem>,
    private val onSettingChanged: (SettingsItem, Boolean) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    inner class SettingsViewHolder(private val binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SettingsItem) {
            binding.apply {
                tvSettingTitle.text = item.title
                tvSettingDescription.text = item.description
                // Nota: Asegúrate de tener vectores o iconos válidos en res/drawable
                ivSettingIcon.setImageResource(item.iconRes)

                // Configuramos la visibilidad del Switch según el tipo
                if (item.type == SettingsType.SWITCH) {
                    switchSetting.visibility = View.VISIBLE
                    // Evitamos disparar el listener por error al pintar la celda
                    switchSetting.setOnCheckedChangeListener(null)
                    switchSetting.isChecked = item.isChecked

                    switchSetting.setOnCheckedChangeListener { _, isChecked ->
                        item.isChecked = isChecked
                        onSettingChanged(item, isChecked)
                    }
                } else {
                    switchSetting.visibility = View.GONE
                    root.setOnClickListener {
                        onSettingChanged(item, false)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}