package com.example.taskup

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import android.widget.ArrayAdapter
import com.google.android.material.textfield.TextInputEditText


class AddTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var projectsList: List<Project>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dbHelper = DatabaseHelper(this)

        // Back Icon Link
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        val userIdString = intent.getStringExtra("userId")
        val userId = userIdString?.toIntOrNull() ?: -1 // Default value if conversion fails
        projectsList = if (userId != -1) dbHelper.getProjects(userId) else emptyList()

        // Initialize AutoCompleteTextView with project titles
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.selectProject)
        val projectTitles = projectsList.map { it.projectTitle }.toTypedArray()

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, projectTitles)
        autoCompleteTextView.setAdapter(adapter)

        // Status ChipGroup
        val chipGroupStatus = findViewById<ChipGroup>(R.id.chipGroupStatus)
        var selectedStatus: String = ""
        chipGroupStatus.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = findViewById(checkedId)
            chip?.let {
                selectedStatus = when (chip.text) {
                    "Pending" -> "Pending"
                    "Ongoing" -> "Ongoing"
                    "Done" -> "Done"
                    else -> ""
                }
            }
        }

        // Priority ChipGroup
        val chipGroupPriority = findViewById<ChipGroup>(R.id.chipGroupPriority)
        var selectedPriority: String = ""
        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = findViewById(checkedId)
            chip?.let {
                selectedPriority = when (chip.text) {
                    "Low" -> "Low"
                    "Moderate" -> "Moderate"
                    "High" -> "High"
                    else -> ""
                }
            }
        }

        // Create Button
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener {
            val taskTitle = findViewById<TextInputEditText>(R.id.taskName).text.toString()
            val taskDue = findViewById<TextInputEditText>(R.id.dueDate).text.toString()
            val taskTime = findViewById<TextInputEditText>(R.id.selectTime).text.toString()
            val taskDesc = findViewById<TextInputEditText>(R.id.desc).text.toString()
            val taskStatus = selectedStatus
            val taskPriority = selectedPriority

            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.selectProject)
            val selectedProject = autoCompleteTextView.text.toString()
            val selectedProjectObject = projectsList.find { it.projectTitle == selectedProject }
            val projectId = selectedProjectObject?.projectId ?: -1

            // Add task to the database
            val taskId = dbHelper.addTask(taskTitle, taskDue, taskTime, taskDesc, taskStatus, taskPriority, projectId)
            if (taskId != -1L) {
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

