package com.example.linguistit.model

import com.google.firebase.Timestamp

data class Sprint(
    val date: Timestamp? = null,
    val passed: Boolean = false,
    val score: Int = 0,
    val technicalTopic: String = "",
    val type: String = "",
    val userAnswer: String = ""
)