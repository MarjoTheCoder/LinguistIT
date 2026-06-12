package com.example.linguistit.data

import android.content.Context
import com.example.linguistit.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ProjectDataSource(private val context: Context) {

    fun getProjectsFromJson(): List<Project> {
        val jsonString: String
        try {
            jsonString = context.assets.open("proyectos_mock.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val listProjectType = object : TypeToken<List<Project>>() {}.type
        return Gson().fromJson(jsonString, listProjectType) ?: emptyList()
    }
}