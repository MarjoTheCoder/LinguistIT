package com.example.linguistit.model

import com.google.firebase.Timestamp

data class SprintResult(
    val date: Timestamp = Timestamp.now(),
    val type: String = "",
    val technicalTopic: String = "",
    val score: Int = 0,
    val passed: Boolean = false,
    val userAnswer: String = ""
)