package com.koshake1.notes.viewmodel

import androidx.lifecycle.*
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.NotesRepository
import com.koshake1.notes.liveevent.LiveEvent
import kotlinx.coroutines.launch

class NoteViewModel(private val notesRepository: NotesRepository, var note: Note?) : ViewModel() {

    private val showErrorLiveData = LiveEvent<String>()

    fun updateNote(text: String) {
        note = (note ?: generateNote()).copy(note = text)
    }

    fun updateTitle(text: String) {
        note = (note ?: generateNote()).copy(title = text)
    }

    fun updateColor(newColor: Int) {
        note = (note ?: generateNote()).copy(color = newColor)
    }

    fun saveNote() {
        viewModelScope.launch {
            val noteValue = note ?: return@launch

            try {
                notesRepository.addOrReplaceNote(noteValue)
            } catch (th: Throwable) {
                showErrorLiveData.value = "Error while saving"
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            val noteValue = note ?: return@launch

            try {
                notesRepository.deleteNote(noteValue)
            } catch (th: Throwable) {
                showErrorLiveData.value = "Error while deleting"
            }
        }
    }

    fun showError(): LiveData<String> = showErrorLiveData

    private fun generateNote(): Note {
        return Note()
    }
}