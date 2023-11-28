package com.example.taskup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class ViewProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project)

        val listView: ListView = findViewById(R.id.listView)
        val data = listOf(
            Pair("Project Item 1", "Description 1"),
            Pair("Project Item 2", "Description 2"),
            Pair("Project Item 3", "Description 3"),
            Pair("Project Item 4", "Description 4"),
            Pair("Project Item 5", "Description 5")
        )  // sample data

        val adapter = ListAdapter(this, data)
        listView.adapter = adapter

        // Calculate the total height of the items
        val totalHeight = calculateTotalHeight(listView)

        // Set the height of the ListView
        listView.layoutParams.height = totalHeight
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