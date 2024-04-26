package com.example.noteapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.noteapi.databinding.ActivityNoteDetailsBinding
import retrofit2.Call
import retrofit2.Response

class NoteDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteId = intent.getIntExtra("item_id", 0)

        loadDetails(noteId)

        binding.btnSaveChanges.setOnClickListener {
            updateNote(noteId)
        }
    }

    private fun loadDetails(noteId: Int) {
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        val requestCall = noteService.getNoteById(noteId)

        requestCall.enqueue(object : retrofit2.Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    val note = response.body()
                    note?.let {
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

        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        val editedNote = Note(noteId, title, content)
        val requestCall = noteService.updateNote(editedNote)

        requestCall.enqueue(object : retrofit2.Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {

                if (response.isSuccessful) {

                    Log.e("Success", "Note Updated")
                    finish()
                } else {
                    Toast.makeText(this@NoteDetailsActivity, "Failed to update note", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}