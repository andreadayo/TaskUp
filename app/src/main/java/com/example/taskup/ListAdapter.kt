package com.example.taskup

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

class ListAdapter(private val context: Context, private val data: List<TaskData>) : BaseAdapter() {

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
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false)

        // val taskProject: TextView = view.findViewById(R.id.projectTheme) // should be view - will be fixed when backend is integrated
        val taskName: TextView = view.findViewById(R.id.tvTaskName)
        val taskDueDate: TextView = view.findViewById(R.id.tvTaskDueDate)
        val taskStatus: TextView = view.findViewById(R.id.chipStatus)
        val taskPriority: TextView = view.findViewById(R.id.chipPriority)

        val taskData = data[position]
        // taskProject.text = taskData.projectCategory
        taskName.text = taskData.taskName
        taskDueDate.text = taskData.dueDate
        taskStatus.text = taskData.status
        taskPriority.text = taskData.priority

        return view
    }
}


