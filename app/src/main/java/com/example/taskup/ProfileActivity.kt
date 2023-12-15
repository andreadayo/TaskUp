package com.example.taskup

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.switchmaterial.SwitchMaterial

class ProfileActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize SharedPreferences
        sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        val username = sharedPreferences.getString("username", "")

        if (username.isNullOrEmpty()) {
            // Handle the case where the username is empty or null
        } else {
            // Continue with retrieving email based on the username
            val tvName = findViewById<TextView>(R.id.tvName)
            tvName.text = username

            val userEmail: String? = dbHelper.getEmailFromUsername(username)

            // Check if email is not null and set it in TextView, otherwise use a default value
            val tvEmail = findViewById<TextView>(R.id.tvEmail)
            tvEmail.text = userEmail ?: "Default Email"
        }

        // Back Icon Link
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        // Logout Dialog
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        // DarkMode Switch
        val switchDarkMode: MaterialSwitch = findViewById(R.id.switchDarkMode)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // Handle dark mode state change (isChecked)
            if (isChecked) {
                // Dark mode is enabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Dark mode is disabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnConfirmLogout = dialog.findViewById<Button>(R.id.btnConfirmLogout)

        btnConfirmLogout.setOnClickListener{
            // logout - remove session

            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        dialog.show()
    }
}