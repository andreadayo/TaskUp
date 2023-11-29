package com.example.taskup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Already have an account? Redirect to Login
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)
        tvLoginLink.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }

        // Fields
        val u_email = findViewById<TextInputLayout>(R.id.tfEmail)
        val u_name = findViewById<TextInputLayout>(R.id.tfUsername)
        val u_pass = findViewById<TextInputLayout>(R.id.tfPassword)

        val email = u_email.editText?.text.toString()
        val username = u_name.editText?.text.toString()
        val password = u_pass.editText?.text.toString()

        // ***** TEMPORARY LINK: for frontend testing
        // After signup should redirect to Login
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        btnSignup.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }
    }
}