package com.example.noteapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

// This class is a custom adapter used to fill the list view with note data
class CustomListAdapter (context: Context, notes: List<Note>) :
    ArrayAdapter<Note>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        // If the view is not reused, inflate the list item layout from XML
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        // Get the note object for the current position
        val note = getItem(position)

        // title field
        val noteTitle = view!!.findViewById<TextView>(R.id.tv_title)
        noteTitle.text = note?.title

        // content field
        val noteContent = view.findViewById<TextView>(R.id.tv_content)
        noteContent.text = note?.content

        return view

    }

}

