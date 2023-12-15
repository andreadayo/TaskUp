package com.example.taskup

import android.content.ClipData
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class ListAdapter(private val context: Context, private val data: List<Task>) : BaseAdapter() {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

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
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_item_task,
            parent,
            false
        )

        // ... other view initialization ...

        val taskName: TextView = view.findViewById(R.id.tvTaskName)
        val taskDueDate: TextView = view.findViewById(R.id.tvTaskDueDate)
        val taskStatus: TextView = view.findViewById(R.id.chipStatus)
        val taskPriority: TextView = view.findViewById(R.id.chipPriority)

        val taskData = data[position]
        taskName.text = taskData.taskTitle
        taskDueDate.text = taskData.taskDue
        taskStatus.text = taskData.taskStatus
        taskPriority.text = taskData.taskPriority

        val checkbox: CheckBox = view.findViewById(R.id.checkBox)

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            val taskData = data[position]
            if (isChecked) {
                // Checkbox is checked, update the status to "Done"
                dbHelper.updateTaskStatus(taskData.taskId, "Done")
            } else {
                // Checkbox is unchecked, you can handle this case if needed
            }

            // Refresh the list after updating the status
            notifyDataSetChanged()
        }

        return view
    }
}

