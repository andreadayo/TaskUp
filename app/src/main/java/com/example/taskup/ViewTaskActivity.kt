package com.example.taskup

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class ViewTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var taskId: Long = -1
    private var selectedStatus: String = ""
    private var selectedPriority: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_task)

        dbHelper = DatabaseHelper(this)
        taskId = intent.getLongExtra("TASK_ID", -1)

        val chipGroupStatus = findViewById<ChipGroup>(R.id.chipGroupStatus)
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

        val chipGroupPriority = findViewById<ChipGroup>(R.id.chipGroupPriority)
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

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        val btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            deleteTask()
        }

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            saveTask()
        }
    }

    private fun deleteTask() {
        val rowsAffected = dbHelper.deleteTask(taskId.toInt())
        if (rowsAffected) {
            finish()
        } else {
            // Handle deletion failure if needed
        }
    }

    private fun saveTask() {
        val updatedTitle = findViewById<TextInputLayout>(R.id.taskNameUpdate).editText?.text.toString()
        val updatedDueDate= findViewById<TextInputLayout>(R.id.dueDateUpdate).editText?.text.toString()
        val updatedTime = findViewById<TextInputLayout>(R.id.selectTimeUpdate).editText?.text.toString()
        val updatedDescription = findViewById<TextInputLayout>(R.id.descUpdate).editText?.text.toString()

        val rowsAffected = dbHelper.updateTask(taskId.toInt(), updatedTitle, updatedDueDate, updatedTime, updatedDescription, selectedStatus, selectedPriority)

        if (rowsAffected) {
            Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to add Task!", Toast.LENGTH_SHORT).show()
        }
    }
}
