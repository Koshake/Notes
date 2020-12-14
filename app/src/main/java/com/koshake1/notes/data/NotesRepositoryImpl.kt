package com.koshake1.notes.data

import androidx.lifecycle.LiveData
import com.koshake1.notes.data.db.FireBaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(private val provider: FireBaseProvider) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> {
        return provider.observeNotes()
    }

    override suspend fun addOrReplaceNote(newNote: Note) = withContext(Dispatchers.IO) {
        provider.addOrReplaceNote(newNote)
    }

    override suspend fun deleteNote(deletedNote: Note) = withContext(Dispatchers.IO) {
        provider.deleteNote(deletedNote)
    }

    override suspend fun getCurrentUser() = withContext(Dispatchers.IO) {
        provider.getCurrentUser()
    }
}