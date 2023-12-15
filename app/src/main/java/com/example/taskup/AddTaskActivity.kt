package com.example.taskup

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var projectsList: List<Project>
    private lateinit var dueDateEditText: TextInputEditText
    private lateinit var dueDateLayout: TextInputLayout
    private lateinit var selectTimeEditText: TextInputEditText
    private lateinit var timeLayout: TextInputLayout
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dbHelper = DatabaseHelper(this)

        // Back Icon Link
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        // Due Date Calendar Picker
        dueDateEditText = findViewById(R.id.dueDate)
        dueDateLayout = findViewById(R.id.tfDueDate)

        dueDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // Time Picker
        selectTimeEditText = findViewById(R.id.selectTime)
        timeLayout = findViewById(R.id.tfTime)

        selectTimeEditText.setOnClickListener {
            showTimePickerDialog()
        }

        // Updated code to retrieve user ID as an integer
        val userId = intent.getIntExtra("USER_ID", -1)
        // Add this to check if userId is correctly received
        Log.i("DatabaseDebug", "Received UserId: $userId")

        // Get all projects for the specific user ID
        val projectsList = dbHelper.getProjects(userId)
        Log.i("DatabaseDebug", "Requested UserId: $userId")

        // Extract titles from projects
        val projectTitles = projectsList.map { it.projectTitle }.toTypedArray()

        // Initialize AutoCompleteTextView with project titles
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.selectProject)
        val projectAdapter = ArrayAdapter(this, R.layout.project_dropdown_item, projectTitles)
        autoCompleteTextView.setAdapter(projectAdapter)

        // Status ChipGroup
        val chipGroupStatus = findViewById<ChipGroup>(R.id.chipGroupStatus)
        var selectedStatus: String = ""

        val chipPending = findViewById<Chip>(R.id.chipPending)
        val chipOngoing = findViewById<Chip>(R.id.chipOngoing)
        val chipDone = findViewById<Chip>(R.id.chipDone)

        val statusChips = listOf(chipPending, chipOngoing, chipDone)

        for (chip in statusChips) {
            chip.setOnClickListener {
                // Uncheck all other chips
                for (otherChip in statusChips) {
                    otherChip.isChecked = false
                }

                // Check the selected chip
                chip.isChecked = true

                // Update the selected status
                selectedStatus = when (chip) {
                    chipPending -> "Pending"
                    chipOngoing -> "Ongoing"
                    chipDone -> "Done"
                    else -> ""
                }

                Log.i("ChipDebug", "Selected chip: ${chip.text}, Checked set")
            }
        }

        // Priority ChipGroup
        val chipGroupPriority = findViewById<ChipGroup>(R.id.chipGroupPriority)
        var selectedPriority: String = ""

        val chipLow = findViewById<Chip>(R.id.chipLow)
        val chipModerate = findViewById<Chip>(R.id.chipModerate)
        val chipHigh = findViewById<Chip>(R.id.chipHigh)

        val priorityChips = listOf(chipLow, chipModerate, chipHigh)

        for (chip in priorityChips) {
            chip.setOnClickListener {
                // Uncheck all other chips
                for (otherChip in priorityChips) {
                    otherChip.isChecked = false
                }

                // Check the selected chip
                chip.isChecked = true

                // Update the selected status
                selectedPriority = when (chip) {
                    chipLow -> "Low"
                    chipModerate -> "Moderate"
                    chipHigh -> "High"
                    else -> ""
                }

                Log.i("ChipDebug", "Selected chip: ${chip.text}, Checked set")
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

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Update the TextInputEditText with the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                updateDueDate(selectedDate.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun updateDueDate(date: Date) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        dueDateEditText.setText(formattedDate)
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // Update the TextInputEditText with the selected time
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                updateSelectedTime(selectedTime.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // Set to true for 24-hour format
        )

        timePickerDialog.show()
    }

    private fun updateSelectedTime(date: Date) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(date)
        selectTimeEditText.setText(formattedTime)
    }
}

