package com.koshake1.notes.viewmodel

import androidx.lifecycle.ViewModel
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.NotesRepositoryImpl

class NoteViewModel(var note: Note?) : ViewModel() {

    fun updateNote(text: String) {
        note = (note ?: generateNote()).copy(note = text)
    }

    fun updateTitle(text: String) {
        note = (note ?: generateNote()).copy(title = text)
    }

    override fun onCleared() {
        super.onCleared()

        note?.let {
            NotesRepositoryImpl.addOrReplaceNote(it)
        }
    }

    private fun generateNote(): Note {
        return Note()
    }
}