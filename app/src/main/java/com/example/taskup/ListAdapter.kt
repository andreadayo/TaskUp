package com.example.taskup

import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import java.text.SimpleDateFormat
import java.util.Locale

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

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_item_task,
            parent,
            false
        )

        val projectName: TextView = view.findViewById(R.id.tvProjectName)
        val taskName: TextView = view.findViewById(R.id.tvTaskName)
        val taskDueDate: TextView = view.findViewById(R.id.tvTaskDueDate)
        val taskDescription: TextView = view.findViewById(R.id.tvTaskDescription)
        val taskStatus: Chip = view.findViewById(R.id.chipStatus)
        val taskPriority: Chip = view.findViewById(R.id.chipPriority)

        val taskData = data[position]
        projectName.text = dbHelper.getProjectNameById(taskData.projectId)
        taskName.text = taskData.taskTitle

        // Format the date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val formattedDate = inputFormat.parse(taskData.taskDue)?.let { outputFormat.format(it) }

        taskDueDate.text = "${formattedDate}, ${taskData.taskTime}"

        taskStatus.text = taskData.taskStatus
        when (taskData.taskStatus) {
            "Pending" -> {
                setChipColors(
                    taskStatus,
                    R.color.md_theme_light_outlineVariant,
                    R.color.md_theme_light_onSurfaceVariant
                )
            }
            "Ongoing" -> {
                setChipColors(
                    taskStatus,
                    R.color.colorQuaternaryContainer,
                    R.color.colorOnQuaternaryContainer
                )
            }
            "Done" -> {
                setChipColors(
                    taskStatus,
                    R.color.md_theme_dark_primary,
                    R.color.md_theme_dark_onPrimary
                )
            }
        }

        taskPriority.text = taskData.taskPriority
        when (taskData.taskPriority) {
            "Low" -> {
                setChipColors(
                    taskPriority,
                    R.color.md_theme_dark_primary,
                    R.color.md_theme_dark_onPrimary
                )
            }
            "Moderate" -> {
                setChipColors(
                    taskPriority,
                    R.color.colorQuaternaryContainer,
                    R.color.colorOnQuaternaryContainer
                )
            }
            "High" -> {
                setChipColors(
                    taskPriority,
                    R.color.md_theme_light_errorContainer,
                    R.color.md_theme_light_tertiary
                )
            }
        }


        taskDescription.text = taskData.taskDesc

        val checkbox: CheckBox = view.findViewById(R.id.checkBox)

        // Set the checked state and listener
        checkbox.isChecked = taskData.taskStatus == "Done"

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbHelper.updateTaskStatus(taskData.taskId, "Done")
            } else {
                // Handle unchecked case if needed
            }

            // Update the data and notify the adapter
            taskData.taskStatus = if (isChecked) "Done" else "Pending"
            notifyDataSetChanged()
        }

        return view
    }

    // Function to set chip colors
    private fun setChipColors(chip: Chip, backgroundColorRes: Int, textColorRes: Int) {
        val bgColor = ContextCompat.getColor(context, backgroundColorRes)
        val textColor = ContextCompat.getColor(context, textColorRes)

        val chipDrawable = chip.chipDrawable?.mutate() as ChipDrawable?
        chipDrawable?.setChipBackgroundColorResource(backgroundColorRes)
        chip.setTextColor(textColor)
    }

}

