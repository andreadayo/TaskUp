package com.example.taskup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    private lateinit var mAddAlarmFab: FloatingActionButton
    private lateinit var mAddPersonFab: FloatingActionButton
    private lateinit var mAddFab: ExtendedFloatingActionButton
    private lateinit var addAlarmActionText: TextView
    private lateinit var addPersonActionText: TextView
    private var isAllFabsVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.activity_app_nav_host_fragment)
        setupWithNavController(binding.bottomNavigationView, navController)

        // Enable DayNight mode
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /*
        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)

        // Check the current mode and set the switch accordingly
        darkModeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val transition = AutoTransition()
            transition.duration = 500 // Set the duration of the transition in milliseconds

            // Use TransitionManager to animate the changes
            TransitionManager.beginDelayedTransition(findViewById(android.R.id.content), transition)

            if (isChecked) {
                // Enable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Enable light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Recreate the activity to apply the new mode
            recreate()


        }*/

        //setContentView(R.layout.activity_app)

        //setContentView(R.layout.activity_app)

        mAddFab = findViewById(R.id.add_fab)
        mAddAlarmFab = findViewById(R.id.add_alarm_fab)
        mAddPersonFab = findViewById(R.id.add_person_fab)
        addAlarmActionText = findViewById(R.id.add_alarm_action_text)
        addPersonActionText = findViewById(R.id.add_person_action_text)

        mAddAlarmFab.visibility = View.GONE
        mAddPersonFab.visibility = View.GONE
        addAlarmActionText.visibility = View.GONE
        addPersonActionText.visibility = View.GONE

        isAllFabsVisible = false
        mAddFab.shrink()

        mAddFab.setOnClickListener {
            isAllFabsVisible = if (!isAllFabsVisible) {
                mAddAlarmFab.show()
                mAddPersonFab.show()
                addAlarmActionText.visibility = View.VISIBLE
                addPersonActionText.visibility = View.VISIBLE
                mAddFab.extend()
                true
            } else {
                mAddAlarmFab.hide()
                mAddPersonFab.hide()
                addAlarmActionText.visibility = View.GONE
                addPersonActionText.visibility = View.GONE
                mAddFab.shrink()
                false
            }
        }

        mAddPersonFab.setOnClickListener {
            showToast("Person Added")
        }

        mAddAlarmFab.setOnClickListener {
            showToast("Alarm Added")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
