package com.example.taskup

import android.content.ClipData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val listView: ListView = view.findViewById(R.id.listView)
        val data = listOf(
            Pair("Item 1", "Description 1"),
            Pair("Item 2", "Description 2"),
            Pair("Item 3", "Description 3"),
            Pair("Item 4", "Description 4"),
            Pair("Item 5", "Description 5")
        )  // sample data

        val adapter = ListAdapter(requireContext(), data)
        listView.adapter = adapter

        // Calculate the total height of the items
        val totalHeight = calculateTotalHeight(listView)

        // Set the height of the ListView
        listView.layoutParams.height = totalHeight

        return view
    }

    private fun calculateTotalHeight(listView: ListView): Int {
        val adapter = listView.adapter
        var totalHeight = 0

        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        return totalHeight
    }
}


