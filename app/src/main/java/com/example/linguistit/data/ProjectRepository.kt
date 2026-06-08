package com.example.linguistit.data

import com.example.linguistit.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProjectRepository(private val dataSource: ProjectDataSource) {

    private val gson = Gson()


    fun getProjects(): List<Project> {
        val jsonString = dataSource.getMockProjectsJson()

        if (jsonString.isNullOrBlank()) {
            return emptyList()
        }

        return try {
            val listType = object : TypeToken<List<Project>>() {}.type
            gson.fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}