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
    lateinit var customAdapter: CustomListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetNotes.setOnClickListener {
            getNoteList()
        }

        binding.lvAllNotes.setOnItemClickListener { parent, view, position, id ->
            val selectedNote = parent.getItemAtPosition(position) as Note
            val intent = Intent(this,NoteDetailsActivity::class.java)
            intent.putExtra("item_id", selectedNote.id)
            startActivity(intent)
        }

        binding.btnCreateNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        binding.lvAllNotes.setOnItemLongClickListener { parent, view, position, id ->
            val selectedNote = parent.getItemAtPosition(position) as Note
            deleteNote(selectedNote.id)
            true
        }
    }

    private fun deleteNote(id: Int) {
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        val requestCall = noteService.deleteNote(id)

        requestCall.enqueue(object  : retrofit2.Callback<Note>{
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful){
                    Toast.makeText(this@MainActivity,"Note deleted", Toast.LENGTH_SHORT).show()
                    getNoteList()
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
        val noteService = ServiceBuilder.buildService(NoteService::class.java)
        val requestCall = noteService.getNoteList()

        requestCall.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val noteList: List<Note>? = response.body() as MutableList
                    println("body ${response.body()}")
                    val mutableNoteList: MutableList<Note> = noteList!!.toMutableList()
//                    val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, mutableNoteList)
//                    binding.lvAllNotes.adapter = arrayAdapter
                    val customAdapter = CustomListAdapter(this@MainActivity, mutableNoteList)
                    binding.lvAllNotes.adapter = customAdapter

                } else {
                    println("Response unsuccessful")
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}