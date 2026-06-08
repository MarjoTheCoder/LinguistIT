package com.example.linguistit.ui.home.projects

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linguistit.MainActivity
import com.example.linguistit.R
import com.example.linguistit.databinding.FragmentProjectsBinding

class ProjectsFragment : Fragment(R.layout.fragment_projects) {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!

    private val projectAdapter by lazy { ProjectAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProjectsBinding.bind(view)

        setupRecyclerView()
        loadProjectsWithAnimation()
    }

    private fun setupRecyclerView() {
        binding.rvProjects.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadProjectsWithAnimation() {
        (activity as? MainActivity)?.showLoading(true)

        binding.root.postDelayed({
            (activity as? MainActivity)?.showLoading(false)

        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}