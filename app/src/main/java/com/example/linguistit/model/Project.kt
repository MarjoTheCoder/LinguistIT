package com.example.linguistit.model

data class Project(
    val id: String,
    val name: String,
    val description: String,
    val progress: Int,
    val status: String,
    val category: String,
    val technologies: List<String>,
    val updated_at: String
)