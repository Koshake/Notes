package com.koshake1.notes.data

import androidx.lifecycle.LiveData
import com.koshake1.notes.data.db.FireBaseProvider

class NotesRepositoryImpl(private val provider: FireBaseProvider) : NotesRepository {

    override fun observeNotes(): LiveData<List<Note>> {
        return provider.observeNotes()
    }

    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        return provider.addOrReplaceNote(newNote)
    }

    override fun deleteNote(deletedNote: Note): LiveData<Result<Note>> {
        return provider.deleteNote(deletedNote)
    }

    override fun getCurrentUser(): User? {
        return provider.getCurrentUser()
    }
}