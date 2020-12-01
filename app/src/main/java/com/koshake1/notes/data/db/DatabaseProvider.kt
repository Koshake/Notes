package com.koshake1.notes.data.db

import androidx.lifecycle.LiveData
import com.koshake1.notes.data.Note

interface DatabaseProvider {
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
}