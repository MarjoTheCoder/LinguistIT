package com.example.linguistit.model

data class SettingsItem(
    val id: Int,
    val title: String,
    val description: String,
    val iconRes: Int,
    val type: SettingsType,
    var isChecked: Boolean = false
)