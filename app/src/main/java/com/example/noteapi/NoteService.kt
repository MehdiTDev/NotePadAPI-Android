package com.example.noteapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteService {

    @GET("notes")
    fun getNoteList(): Call<List<Note>>

    @GET("notes/{id}")
    fun getNoteById(@Path("id")id: Int): Call<Note>

    @POST("notes")
    fun addNote(@Body newNote: Note): Call<Note>

//    @FormUrlEncoded
    @PUT("notes")
    fun updateNote(@Body editedNote: Note): Call<Note>

    @DELETE("notes/{id}")
    fun deleteNote(@Path("id")id: Int): Call<Note>

}