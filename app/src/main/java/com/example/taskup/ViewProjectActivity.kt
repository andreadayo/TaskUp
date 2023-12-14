package com.example.taskup

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ViewProjectActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project)

        dbHelper = DatabaseHelper(this)

        // Back Icon Link
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        val loggedInUsername = "username"
        val userId = dbHelper.getUserIdFromUsername(loggedInUsername)

        // Open Edit Project Dialog
        val btnEditProject = findViewById<ImageView>(R.id.btnEditProject)
        btnEditProject.setOnClickListener {
            val projects = dbHelper.getProjects(userId) // Assuming you have the userId available

            // Selecting the first project's details for demonstration; adjust this based on your logic
            if (projects.isNotEmpty()) {
                val project = projects[0] // Selecting the first project, change as needed

                val projectId = project.projectId
                val currentTitle = project.projectTitle
                val currentStatus = project.projectStatus

                showEditProjectDialog(projectId, currentTitle, currentStatus)
            } else {
                // Handle the case where no projects are available for the user
            }
        }



        // Add Task Redirect
        val fabButtonNewTask = findViewById<ExtendedFloatingActionButton>(R.id.fabButtonNewTask)
        fabButtonNewTask.setOnClickListener {
            val i = Intent(this,AddTaskActivity::class.java)
            startActivity(i)
        }

        // ***** TEMPORARY DATA: for frontend testing
        val listView: ListView = findViewById(R.id.listView)
        val data = listOf(
            TaskData("Project Category 1", "Task Name 1", "November 3 2023", "Ongoing", "Low"),
            TaskData("Project Category 2", "Task Name 2", "November 2 2023", "Pending", "High"),
            TaskData("Project Category 3", "Task Name 3", "November 11 2023", "Done", "Moderate"),
        )

        val adapter = ListAdapter(this, data)
        listView.adapter = adapter

        // Set item click listener
        listView.setOnItemClickListener { _, _, position, _ ->
            // Get the selected item data
            val selectedItem = data[position]

            // Start ViewTaskActivity with relevant information
            val intent = Intent(this, ViewTaskActivity::class.java).apply {
                // Pass relevant information to ViewTaskActivity
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

    private fun showEditProjectDialog(projectId: Int, currentTitle: String, currentStatus: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_project)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val projectTitleInput = dialog.findViewById<TextView>(R.id.editProjectInput)
        projectTitleInput.text = currentTitle

        val btnStatus1 = dialog.findViewById<Button>(R.id.chipYellow)
        val btnStatus2 = dialog.findViewById<Button>(R.id.chipPink)
        val btnStatus3 = dialog.findViewById<Button>(R.id.chipPurple)
        val btnStatus4 = dialog.findViewById<Button>(R.id.chipBlue)

        when (currentStatus) {
            "Status1" -> {
                btnStatus1.isSelected = true
                btnStatus2.isSelected = false
                btnStatus3.isSelected = false
                btnStatus4.isSelected = false
            }
            "Status2" -> {
                btnStatus1.isSelected = false
                btnStatus2.isSelected = true
                btnStatus3.isSelected = false
                btnStatus4.isSelected = false

            }
            "Status3" -> {
                btnStatus1.isSelected = false
                btnStatus2.isSelected = false
                btnStatus3.isSelected = true
                btnStatus4.isSelected = false

            }
            "Status4" -> {
                btnStatus1.isSelected = false
                btnStatus2.isSelected = false
                btnStatus3.isSelected = false
                btnStatus4.isSelected = true

            }
        }

        val btnSave = dialog.findViewById<Button>(R.id.btnSaveProject)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelProject)

        btnSave.setOnClickListener {
            // Check which status button is selected and update the status accordingly
            val updatedStatus = when {
                btnStatus1.isSelected -> "Status1"
                btnStatus2.isSelected -> "Status2"
                btnStatus3.isSelected -> "Status3"
                btnStatus4.isSelected -> "Status4"
                else -> "DefaultStatus"
            }

            val isUpdated = dbHelper.updateProject(projectId, currentTitle, updatedStatus)

            if (isUpdated) {
                Toast.makeText(this, "Project updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update project!", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
