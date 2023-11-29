package com.example.taskup

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private lateinit var navController: NavController

    private lateinit var fabButtonNewTask: FloatingActionButton
    private lateinit var fabButtonNewProject: FloatingActionButton
    private lateinit var mAddFab: ExtendedFloatingActionButton
    private lateinit var fabTextNewTask: TextView
    private lateinit var fabTextNewProject: TextView
    private var isAllFabsVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        // Redirect to Add Task
        fabButtonNewTask.setOnClickListener {
            val i = Intent(this,AddTaskActivity::class.java)
            startActivity(i)
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

        btnCreate.setOnClickListener{
            // add and save project to database
        }
        btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

}
