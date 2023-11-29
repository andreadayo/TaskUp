package com.example.taskup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class CalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // ***** TEMPORARY DATA: for frontend testing
        val listView: ListView = view.findViewById(R.id.listViewToday)
        val data = listOf(
            TaskData("Project Category 1", "Task Name 1", "November 3 2023", "Ongoing", "Low"),
            TaskData("Project Category 2", "Task Name 2", "November 2 2023", "Pending", "High"),
            TaskData("Project Category 3", "Task Name 3", "November 11 2023", "Done", "Moderate"),
        )

        val adapter = ListAdapter(requireContext(), data)
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
        var totalHeight = 20

        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight + 24
        }

        return totalHeight
    }
}
