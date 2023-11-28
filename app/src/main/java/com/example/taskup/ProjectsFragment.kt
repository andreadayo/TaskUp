package com.example.taskup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import com.google.android.flexbox.FlexboxLayout

class ProjectsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_projects, container, false)

        // temporary link for testing frontend (Set an onClick instance for all include)
        val flexboxLayout = view.findViewById<FlexboxLayout>(R.id.flexboxLayout)
        for (i in 0 until flexboxLayout.childCount) {
            val cardProject = flexboxLayout.getChildAt(i).findViewById<CardView>(R.id.cardProject)
            cardProject.setOnClickListener {
                val intent = Intent(requireContext(), ViewProjectActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }
}
