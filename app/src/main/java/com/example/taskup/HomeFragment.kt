package com.example.taskup

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView

class HomeFragment : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tasksList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(requireContext())

        // Retrieve user ID from SharedPreferences
        val userId = sharedPreferences.getInt("userId", -1)

        // Retrieve projects list from the database
        tasksList = dbHelper.getTasks(userId)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val listView: ListView = view.findViewById(R.id.listView)

        // Show only the first three tasks
        val tasksToShow = tasksList.take(3)

        // Create and set the adapter
        val adapter = ListAdapter(requireContext(), tasksToShow)
        listView.adapter = adapter

        // Calculate the total height of the items
        val totalHeight = calculateTotalHeight(listView)

        // Set the height of the ListView
        listView.layoutParams.height = totalHeight

        return view
    }

    // Function to adjust height based on # of items in the listView
    private fun calculateTotalHeight(listView: ListView): Int {
        val adapter = listView.adapter
        var totalHeight = 0

        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight + listView.dividerHeight
        }

        return totalHeight
    }
}



