package com.example.linguistit.model

import com.google.firebase.Timestamp

data class Project(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: String = "",
    val progress: Int = 0,
    val startDate: Timestamp = Timestamp.now(),
    val githubUrl: String = ""
)