package com.koshake1.notes.data.db

import com.koshake1.notes.data.Note
import com.koshake1.notes.data.User
import kotlinx.coroutines.flow.Flow

interface DatabaseProvider {
    fun getCurrentUser(): User?
    fun observeNotes(): Flow<List<Note>>
    suspend fun addOrReplaceNote(newNote: Note)
    suspend fun deleteNote(deletedNote: Note)
}