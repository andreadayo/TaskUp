package com.example.taskup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.NavController
import com.example.taskup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            val i = Intent(this,SignupActivity::class.java)
            startActivity(i)
        }
    }


}
