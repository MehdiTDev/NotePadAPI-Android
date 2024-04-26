package com.example.noteapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomListAdapter (context: Context, notes: List<Note>) :
    ArrayAdapter<Note>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val note = getItem(position)

        val noteTitle = view!!.findViewById<TextView>(R.id.tv_title)
        noteTitle.text = note?.title

        val noteContent = view.findViewById<TextView>(R.id.tv_content)
        noteContent.text = note?.content

        return view

    }

}

