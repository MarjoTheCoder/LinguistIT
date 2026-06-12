package com.example.linguistit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linguistit.R
import com.example.linguistit.model.Sprint
import com.example.linguistit.utils.DateUtils

class SprintAdapter(private var sprints: List<Sprint> = emptyList()) :
    RecyclerView.Adapter<SprintAdapter.SprintViewHolder>() {

    class SprintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTopic: TextView = view.findViewById(R.id.tvItemTopic)
        val tvDate: TextView = view.findViewById(R.id.tvItemDate)
        val tvScore: TextView = view.findViewById(R.id.tvItemScore)
        val vStatusIndicator: View = view.findViewById(R.id.vStatusIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SprintViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sprint, parent, false)
        return SprintViewHolder(view)
    }

    override fun onBindViewHolder(holder: SprintViewHolder, position: Int) {
        val sprint = sprints[position]

        holder.tvTopic.text = sprint.technicalTopic
        holder.tvScore.text = "${sprint.score}/100"

        holder.tvDate.text = DateUtils.obtenerFechaFormateada(sprint.date)

        val colorIndicator = if (sprint.passed) 0xFF4CAF50.toInt() else 0xFFF44336.toInt()
        holder.vStatusIndicator.setBackgroundColor(colorIndicator)
    }

    override fun getItemCount(): Int = sprints.size

    fun updateData(nuevosSprints: List<Sprint>) {
        this.sprints = nuevosSprints
        notifyDataSetChanged()
    }
}