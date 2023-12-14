package com.example.taskup

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskup.DatabaseHelper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

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

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.selectProject)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedProject = parent.adapter.getItem(position) as String
            val selectedProjectObject = projectsList.find { it.title== selectedProject }



            // Create Button
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener {
            val title = findViewById<TextInputLayout>(R.id.taskName).editText?.text.toString()
            val dueDate = findViewById<TextInputLayout>(R.id.dueDate).editText?.text.toString()
            val time = findViewById<TextInputLayout>(R.id.selectTime).editText?.text.toString()
            val description = findViewById<TextInputLayout>(R.id.desc).editText?.text.toString()
            val status = selectedStatus
            val priority = selectedPriority
            val projectId = selectedProjectObject?.projectId ?: -1

            // Add task to the database
            val taskId = dbHelper.addTask(title, dueDate, time, description, status, priority, projectId)
            if (taskId != -1L) {
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
            }
        }
        }
    }
}
