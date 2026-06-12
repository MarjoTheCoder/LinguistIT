package com.example.linguistit.model

import com.google.firebase.Timestamp

data class SavedTerm(
    val term: String = "",
    val definition: String = "",
    val category: String = "",
    val status: String = "learning",
    val savedAt: Timestamp = Timestamp.now()
)