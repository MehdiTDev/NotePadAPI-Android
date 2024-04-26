package com.example.noteapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapi.databinding.ActivityAddNoteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            addNote()
        }
    }

    private fun addNote() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()

        val newNote = Note(-1, title, content)
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        val requestCall = noteService.addNote(newNote)

        requestCall.enqueue(object: Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful){
                    finish()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}