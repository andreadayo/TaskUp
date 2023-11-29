package com.example.taskup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Don't have an account? Redirect to Signup
        val tvSignupLink = findViewById<TextView>(R.id.tvSignupLink)
        tvSignupLink.setOnClickListener {
            val i = Intent(this,SignupActivity::class.java)
            startActivity(i)
        }

        // Fields
        val u_name = findViewById<TextInputLayout>(R.id.tfUsername)
        val u_pass = findViewById<TextInputLayout>(R.id.tfPassword)

        val username = u_name.editText?.text.toString()
        val password = u_pass.editText?.text.toString()

        // ***** TEMPORARY LINK: for frontend testing
        // After successful login should redirect to App
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val i = Intent(this,AppActivity::class.java)
            startActivity(i)
        }


    }
}