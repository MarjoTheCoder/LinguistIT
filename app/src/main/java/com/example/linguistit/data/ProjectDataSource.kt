package com.example.linguistit.data

import android.content.Context
import java.io.IOException

class ProjectDataSource(private val context: Context) {

    fun getMockProjectsJson(fileName: String = "proyectos_mock.json"): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}