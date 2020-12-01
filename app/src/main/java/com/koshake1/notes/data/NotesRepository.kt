package com.koshake1.notes.data

import androidx.lifecycle.LiveData

interface NotesRepository {
    fun getCurrentUser() : User?
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note) : LiveData<Result<Note>>
}