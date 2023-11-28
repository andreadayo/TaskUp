package com.example.taskup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import com.google.android.material.chip.Chip

class TasksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        // Find the chip within the inflated layout
        val chipAllTasks = view.findViewById<Chip>(R.id.chipAllTasks)
        chipAllTasks.setOnClickListener {
            val i = Intent(requireContext(), ViewTaskActivity::class.java)
            startActivity(i)
        }

        return view
    }
}
