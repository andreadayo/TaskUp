package com.example.taskup

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProjectAutoCompleteAdapter(
    context: Context,
    projects: List<Project>
) : ArrayAdapter<Project>(context, R.layout.project_dropdown_item, projects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val project = getItem(position)

        // Customize the appearance of the selected item if needed
        (view as? TextView)?.text = project?.projectTitle

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val project = getItem(position)

        // Customize the appearance of each dropdown item if needed
        (view as? TextView)?.text = project?.projectTitle

        return view
    }
}