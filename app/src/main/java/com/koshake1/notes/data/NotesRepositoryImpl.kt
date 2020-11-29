package com.koshake1.notes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NotesRepositoryImpl : NotesRepository {
    private val notes: MutableList<Note> = mutableListOf()

    private val allNotes = MutableLiveData(getListForNotify())

    private fun getListForNotify(): List<Note> = notes.toMutableList().also {
        it.reverse()
    }

    override fun observeNotes(): LiveData<List<Note>> {
        return allNotes
    }

    override fun addOrReplaceNote(newNote: Note) {
        notes.find { it.id == newNote.id }?.let {
            if (it == newNote) {
                return
            }
            notes.remove(it)
        }
        notes.add(newNote)
        allNotes.postValue(
            getListForNotify()
        )
    }
}