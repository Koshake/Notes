package com.koshake1.notes.data

interface NotesRepository {
    fun getAllNotes() : List<Note>
}