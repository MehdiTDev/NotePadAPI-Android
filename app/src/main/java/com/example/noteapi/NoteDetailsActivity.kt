package com.example.noteapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.noteapi.databinding.ActivityNoteDetailsBinding
import retrofit2.Call
import retrofit2.Response

// This class represents the activity for viewing and updating note details
class NoteDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Extract the note ID from the intent that launched this activity
        val noteId = intent.getIntExtra("item_id", 0)

        // Load the note details from the server upon activity creation
        loadDetails(noteId)

        // ClickListener for "Save Changes" button to update the note
        binding.btnSaveChanges.setOnClickListener {
            updateNote(noteId)
        }
    }

    private fun loadDetails(noteId: Int) {
        // Building the service for NoteService interface
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        // Making a network request call to get the note with the specified ID
        val requestCall = noteService.getNoteById(noteId)

        // Enqueuing the request asynchronously using Retrofit callback
        requestCall.enqueue(object : retrofit2.Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    val note = response.body()
                    note?.let {
                        // Set the edit text fields with the fetched title and content
                        binding.etNewTitle.setText(note.title)
                        binding.etNewContent.setText(note.content)
                    }
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun updateNote(noteId: Int) {

        val title = binding.etNewTitle.text.toString()
        val content = binding.etNewContent.text.toString()

        // Building the service for NoteService interface
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        // Create a new Note object with the updated title and content
        val editedNote = Note(noteId, title, content)
        // Make a network request to update the note
        val requestCall = noteService.updateNote(editedNote)

        // Enqueuing the request asynchronously using Retrofit callback
        requestCall.enqueue(object : retrofit2.Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {

                if (response.isSuccessful) {

                    Log.e("Success", "Note Updated")
                    finish() // Close the activity after successful update
                } else {
                    Toast.makeText(this@NoteDetailsActivity, "Failed to update note", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace() // Log or handle the error
            }
        })
    }
}