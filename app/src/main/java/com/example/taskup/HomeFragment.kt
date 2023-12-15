package com.example.taskup

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

data class TaskData(
    val projectCategory: String,
    val taskName: String,
    val dueDate: String,
    val status: String,
    val priority: String,
)

class HomeFragment : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dbHelper = DatabaseHelper(requireContext())

        val totalProjects = view.findViewById<TextView>(R.id.totalProjects)
        val totalTasks = view.findViewById<TextView>(R.id.totalTasks)
        dbHelper = DatabaseHelper(requireContext())

        val totalTaskCount = dbHelper.getTotalTaskCount()
        val totalProjectCount = dbHelper.getTotalProjectCount()

        totalProjects.text = totalProjectCount.toString()
        totalTasks.text = totalTaskCount.toString()

        // Profile Link
        val btnProfile = view.findViewById<ImageView>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            val username = intent.getStringExtra("USERNAME")
            val user = "$username"
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        // ***** TEMPORARY DATA: for frontend testing
        val listView: ListView = view.findViewById(R.id.listView)
        val data = listOf(
            TaskData("Project Category 1", "Task Name 1", "November 3 2023", "Ongoing", "Low"),
            TaskData("Project Category 2", "Task Name 2", "November 2 2023", "Pending", "High"),
            TaskData("Project Category 3", "Task Name 3", "November 11 2023", "Done", "Moderate"),
        )

        val adapter = ListAdapter(requireContext(), data)
        listView.adapter = adapter

        // Set item click listener
        listView.setOnItemClickListener { _, _, position, _ ->
            // Get the selected item data
            val selectedItem = data[position]

            // Start ViewTaskActivity with relevant information
            val intent = Intent(requireContext(), ViewTaskActivity::class.java).apply {
                putExtra("projectCategory", selectedItem.projectCategory)
                putExtra("taskName", selectedItem.taskName)
                putExtra("dueDate", selectedItem.dueDate)
                putExtra("status", selectedItem.status)
                putExtra("priority", selectedItem.priority)
            }
            startActivity(intent)
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
        var totalHeight = 20

        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight + 24
        }

        return totalHeight
    }
}



