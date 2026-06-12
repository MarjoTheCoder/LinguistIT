package com.example.linguistit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linguistit.R
import com.example.linguistit.model.Project
import com.google.android.material.chip.Chip

class ProjectAdapter(
    private var projects: List<Project> = emptyList(),
    private val onStatusBadgeClick: (Project) -> Unit
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvProjectName)
        val tvDesc: TextView = view.findViewById(R.id.tvProjectDesc)
        val chipStatus: Chip = view.findViewById(R.id.chipProjectStatus)
        val pbProgress: ProgressBar = view.findViewById(R.id.pbProjectProgress)
        val tvPercent: TextView = view.findViewById(R.id.tvProgressPercent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]

        holder.tvName.text = project.name
        holder.tvDesc.text = project.description
        holder.pbProgress.progress = project.progress
        holder.tvPercent.text = "${project.progress}%"

        // Mapeo dinámico del texto del estatus
        holder.chipStatus.text = project.status

        val dynamicColor = when (project.status.lowercase().trim()) {
            "backlog", "por hacer" -> 0xFF757575.toInt()
            "en desarrollo", "working" -> 0xFFFF9800.toInt()
            "qa", "revisión" -> 0x3F51B5.toInt()
            "completado", "done" -> 0xFF4CAF50.toInt()
            "bloqueado", "blocked" -> 0xFFF44336.toInt()
            else -> 0xFF607D8B.toInt()
        }

        holder.chipStatus.setChipBackgroundColorResource(android.R.color.transparent)
        holder.chipStatus.chipBackgroundColor = android.content.res.ColorStateList.valueOf(dynamicColor)

        holder.chipStatus.setOnClickListener { onStatusBadgeClick(project) }
    }

    override fun getItemCount(): Int = projects.size

    fun updateData(nuevosProyectos: List<Project>) {
        this.projects = nuevosProyectos
        notifyDataSetChanged()
    }
}