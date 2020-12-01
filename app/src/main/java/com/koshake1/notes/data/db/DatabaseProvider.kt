package com.koshake1.notes.data.db

import androidx.lifecycle.LiveData
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.User

interface DatabaseProvider {
    fun getCurrentUser() : User?
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
}