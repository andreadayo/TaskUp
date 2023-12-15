package com.example.taskup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.flexbox.FlexboxLayout

interface OnProjectItemClickListener {
    fun onProjectItemClick(project: Project)
}

class ProjectsFragment : Fragment(), OnProjectItemClickListener {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var projectsList: List<Project>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(requireContext())

        // Retrieve user ID from SharedPreferences
        val userId = sharedPreferences.getInt("userId", -1)

        // Retrieve projects list from the database
        projectsList = dbHelper.getProjects(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_projects, container, false)

        val listView: ListView = view.findViewById(R.id.projectListView)

        // Create and set the adapter
        val adapter = ListAdapterProject(requireContext(), projectsList, this)
        listView.adapter = adapter

        // Set item click listener
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProject = projectsList[position]
            onProjectItemClick(selectedProject)
        }

        // Calculate the total height of the items
        val totalHeight = calculateTotalHeight(listView)

        // Set the height of the ListView
        listView.layoutParams.height = totalHeight

        return view
    }

    // Function to adjust height based on # of items in the listView
    private fun calculateTotalHeight(listView: ListView): Int {
        val adapter = listView.adapter
        var totalHeight = 100

        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight + listView.dividerHeight
        }

        return totalHeight
    }

    override fun onProjectItemClick(project: Project) {
        val intent = Intent(requireContext(), ViewProjectActivity::class.java).apply {
            putExtra("projectId", project.projectId)
            putExtra("projectTitle", project.projectTitle)
            putExtra("projectStatus", project.projectStatus)
            putExtra("userId", project.userId)
        }
        startActivity(intent)
    }
}
