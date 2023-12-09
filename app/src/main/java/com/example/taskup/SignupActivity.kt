package com.example.taskup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog

class SignupActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        dbHelper = DatabaseHelper(this)

        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)
        tvLoginLink.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        val u_email = findViewById<TextInputLayout>(R.id.tfEmail)
        val u_name = findViewById<TextInputLayout>(R.id.tfUsername)
        val u_pass = findViewById<TextInputLayout>(R.id.tfPassword)

        val btnSignup = findViewById<Button>(R.id.btnSignup)
        btnSignup.setOnClickListener {
            val email = u_email.editText?.text.toString()
            val username = u_name.editText?.text.toString()
            val password = u_pass.editText?.text.toString()

            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                val userId = dbHelper.addUser(email, username, password)

                if (userId != -1L) {
                    // User added successfully, show dialog and navigate to login activity
                    showSuccessDialogAndNavigate()
                } else {
                    // Handle failure to add user (perhaps show an error message)
                }
            } else {
                // Handle empty fields (perhaps show a toast or error message)
            }
        }
    }

    private fun showSuccessDialogAndNavigate() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Sign up successful!")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                navigateToLogin()
            }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity to prevent going back to sign-up using the back button
    }

    override fun onDestroy() {
        dbHelper.close() // Close the database when the activity is destroyed
        super.onDestroy()
    }
}
