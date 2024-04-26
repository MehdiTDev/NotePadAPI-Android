package com.example.noteapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.noteapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ClickListener for "Get Notes" button to fetch all notes
        binding.btnGetNotes.setOnClickListener {
            getNoteList()
        }

        // Navigating to NoteDetailsActivity by ClickListener
        binding.lvAllNotes.setOnItemClickListener { parent, view, position, id ->
            val selectedNote = parent.getItemAtPosition(position) as Note
            val intent = Intent(this,NoteDetailsActivity::class.java)
            intent.putExtra("item_id", selectedNote.id)
            startActivity(intent)
        }

        // Initialising the AddNoteActivity by ClickListener
        binding.btnCreateNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // Delete selected note with LongClickListener
        binding.lvAllNotes.setOnItemLongClickListener { parent, view, position, id ->
            val selectedNote = parent.getItemAtPosition(position) as Note
            deleteNote(selectedNote.id)
            true
        }
    }

    private fun deleteNote(id: Int) {
        // Building the service for NoteService interface using ServiceBuilder
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        // Making a network request call to delete the note with the specified ID
        val requestCall = noteService.deleteNote(id)

        // Enqueuing the request asynchronously using Retrofit callback
        requestCall.enqueue(object  : retrofit2.Callback<Note>{
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful){
                    Toast.makeText(this@MainActivity,"Note deleted", Toast.LENGTH_SHORT).show()
                    getNoteList() // Refresh the list after successful deletion
                } else {
                    Toast.makeText(this@MainActivity,"Failed to delete note", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun getNoteList() {
        // Building the service for NoteService interface using ServiceBuilder
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        // Making a request call to add a new note
        val requestCall = noteService.getNoteList()

        // Enqueuing the request call to execute asynchronously
        requestCall.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val noteList: List<Note>? = response.body() as MutableList
                    println("body ${response.body()}")
                    val mutableNoteList: MutableList<Note> = noteList!!.toMutableList()
                    // Creating a custom adapter to populate the list view
                    val customAdapter = CustomListAdapter(this@MainActivity, mutableNoteList)
                    binding.lvAllNotes.adapter = customAdapter

                } else {
                    println("Response unsuccessful") // Log or handle the error
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}