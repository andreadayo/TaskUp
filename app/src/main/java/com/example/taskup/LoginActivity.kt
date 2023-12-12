package com.example.taskup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val tvSignupLink = findViewById<TextView>(R.id.tvSignupLink)
        tvSignupLink.setOnClickListener {
            val i = Intent(this, SignupActivity::class.java)
            startActivity(i)
        }

        val u_name = findViewById<TextInputLayout>(R.id.tfUsername)
        val u_pass = findViewById<TextInputLayout>(R.id.tfPassword)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val username = u_name.editText?.text.toString()
            val password = u_pass.editText?.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = dbHelper.getUser(username, password)

                if (user != null) {
                    // User authenticated, navigate to AppActivity
                    val i = Intent(this, AppActivity::class.java)
                    i.putExtra("USERNAME", username)
                    startActivity(i)
                    finish() // Finish this activity to prevent going back to login using the back button
                } else {
                    // Show dialog for invalid credentials
                    showInvalidCredentialsDialog()
                }
            } else {
                showErrorDialog()
            }
        }
    }

        private fun showInvalidCredentialsDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Username or password does not match")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.show()
    }

         private fun showErrorDialog() {
         val dialogBuilder = AlertDialog.Builder(this)
         dialogBuilder.setMessage("ERROR 404")
             .setCancelable(false)
             .setPositiveButton("OK") { dialog, _ ->
                 dialog.dismiss()
            }
          val alert = dialogBuilder.create()
          alert.show()
    }

    override fun onDestroy() {
        dbHelper.close() // Close the database when the activity is destroyed
        super.onDestroy()
    }
}

