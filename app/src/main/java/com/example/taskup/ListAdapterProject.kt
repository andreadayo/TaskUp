package com.example.taskup

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

class ListAdapterProject(private val context: Context, private val data: List<Project>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.card_project_wide, parent, false)

        // val taskProject: TextView = view.findViewById(R.id.projectTheme) // should be view - will be fixed when backend is integrated
        val projectName: TextView = view.findViewById(R.id.tvProjectName)

        val taskData = data[position]
        projectName.text = taskData.projectTitle
        return view
    }
}


