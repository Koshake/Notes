package com.koshake1.notes.viewmodel

import androidx.lifecycle.*
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.NotesRepository
import com.koshake1.notes.liveevent.LiveEvent

class NoteViewModel(private val notesRepository: NotesRepository, var note: Note?) : ViewModel() {

    private val showErrorLiveData = LiveEvent<String>()

    private val lifecycleOwner: LifecycleOwner = LifecycleOwner { viewModelLifecycle }
    private val viewModelLifecycle = LifecycleRegistry(lifecycleOwner).also {
        it.currentState = Lifecycle.State.RESUMED
    }

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
        note?.let { note ->
            notesRepository.addOrReplaceNote(note).observe(lifecycleOwner) {
                it.onFailure {
                    showErrorLiveData.postValue("Error while saving")
                }
                it.onSuccess {
                    showErrorLiveData.postValue("Successfully saved!")
                }
            }
        }
    }

    fun deleteNote() {
        note?.let { note ->
            notesRepository.deleteNote(note).observe(lifecycleOwner) {
                it.onFailure {
                    showErrorLiveData.postValue("Error while deleting!")
                }
                it.onSuccess {
                    showErrorLiveData.postValue("Successfully deleted!")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelLifecycle.currentState = Lifecycle.State.DESTROYED
    }

    fun showError(): LiveData<String> = showErrorLiveData

    private fun generateNote(): Note {
        return Note()
    }
}