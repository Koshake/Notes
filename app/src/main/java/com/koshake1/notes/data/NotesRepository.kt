package com.koshake1.notes.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getCurrentUser(): User?
    fun observeNotes(): Flow<List<Note>>
    suspend fun addOrReplaceNote(newNote: Note)
    suspend fun deleteNote(deletedNote: Note)
}