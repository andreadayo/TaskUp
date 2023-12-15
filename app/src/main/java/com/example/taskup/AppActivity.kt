package com.example.taskup

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.taskup.databinding.ActivityAppBinding
import androidx.appcompat.app.AppCompatDelegate
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private lateinit var navController: NavController

    private lateinit var fabButtonNewTask: FloatingActionButton
    private lateinit var fabButtonNewProject: FloatingActionButton
    private lateinit var mAddFab: ExtendedFloatingActionButton
    private lateinit var fabTextNewTask: TextView
    private lateinit var fabTextNewProject: TextView
    private var isAllFabsVisible: Boolean = false
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DatabaseHelper(this)

        // For controlling the bottom navbar
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.activity_app_nav_host_fragment)
        setupWithNavController(binding.bottomNavigationView, navController)

        // For controlling the floating action button (FAB)
        mAddFab = findViewById(R.id.add_fab)
        fabButtonNewTask = findViewById(R.id.fabButtonNewTask)
        fabButtonNewProject = findViewById(R.id.fabButtonNewProject)
        fabTextNewTask = findViewById(R.id.fabTextNewTask)
        fabTextNewProject = findViewById(R.id.fabTextNewProject)

        fabButtonNewTask.visibility = View.GONE
        fabButtonNewProject.visibility = View.GONE
        fabTextNewTask.visibility = View.GONE
        fabTextNewProject.visibility = View.GONE

        isAllFabsVisible = false
        mAddFab.shrink()

        mAddFab.setOnClickListener {
            isAllFabsVisible = if (!isAllFabsVisible) {
                fabButtonNewTask.show()
                fabButtonNewProject.show()
                fabTextNewTask.visibility = View.VISIBLE
                fabTextNewProject.visibility = View.VISIBLE
                mAddFab.extend()
                true
            } else {
                fabButtonNewTask.hide()
                fabButtonNewProject.hide()
                fabTextNewTask.visibility = View.GONE
                fabTextNewProject.visibility = View.GONE
                mAddFab.shrink()
                false
            }
        }

        // Open Add Project Dialog
        fabButtonNewProject.setOnClickListener {
            showAddProjectDialog()
        }

        val username = intent.getStringExtra("USERNAME")
        val user = "$username"
        val tvUser = findViewById<TextView>(R.id.tvUser)
        tvUser.text = user

        if (username != null) {
            val userId = dbHelper.getUserId(username)

            // Redirect to Add Task
            fabButtonNewTask.setOnClickListener {
                val i = Intent(this, AddTaskActivity::class.java)
                i.putExtra("USER_ID", userId)
                startActivity(i)

                Log.i("DatabaseDebug", "Passed UserId: $userId")
            }
        } else {
            Log.i("DatabaseDebug", "Username is null")
            // Handle the case where the username is null
        }

    }

    private fun showAddProjectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_project)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCreate = dialog.findViewById<Button>(R.id.btnCreateProject)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelProject)
        val projectTitleInput = dialog.findViewById<TextInputEditText>(R.id.projectTitleInput)

        // Access the root view of the dialog
        val dialogView = dialog.findViewById<View>(R.id.dialogAddProject)

        // Color ChipGroup
        val chipGroupColor = dialogView.findViewById<ChipGroup>(R.id.chipColor)
        var selectedColor: String = ""

        val chipBlue = dialogView.findViewById<Chip>(R.id.chipBlue)
        val chipPurple = dialogView.findViewById<Chip>(R.id.chipPurple)
        val chipPink = dialogView.findViewById<Chip>(R.id.chipPink)
        val chipYellow = dialogView.findViewById<Chip>(R.id.chipYellow)

        val colorChips = listOf(chipBlue, chipPurple, chipPink, chipYellow)

        for (chip in colorChips) {
            chip.setOnClickListener {
                // Uncheck all other chips
                for (otherChip in colorChips) {
                    otherChip?.isChecked = false
                }

                // Check the selected chip
                chip?.isChecked = true

                // Update the selected status
                selectedColor = when (chip) {
                    chipBlue -> "Blue"
                    chipPurple -> "Purple"
                    chipPink -> "Pink"
                    chipYellow -> "Yellow"
                    else -> ""
                }

                Log.i("ChipDebug", "Selected chip: ${chip?.text}, Checked set")
            }
        }

        btnCreate.setOnClickListener {
            val projectTitle = projectTitleInput.text.toString()
            if (projectTitle.isNotEmpty()) {
                val userId = intent.getIntExtra("USER_ID", -1)

                // Call addProject from DatabaseHelper to add the project
                val projectId = dbHelper.addProject(projectTitle, selectedColor, userId)

                if (projectId != -1L) {
                    // Project added successfully
                    Toast.makeText(this, "Project added!", Toast.LENGTH_SHORT).show()
                } else {
                    // Failed to add project
                    Toast.makeText(this, "Failed to add project!", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a project title", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



}
