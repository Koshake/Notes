package com.koshake1.notes.viewmodel

import androidx.lifecycle.*
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.notesRepository
import com.koshake1.notes.liveevent.LiveEvent


class NoteViewModel(var note: Note?) : ViewModel() {

    private val showErrorLiveData = LiveEvent<Boolean>()

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

    fun saveNote() {
        note?.let { note ->
            notesRepository.addOrReplaceNote(note).observe(lifecycleOwner) {
                it.onFailure {
                    showErrorLiveData.postValue(true)
                }
                it.onSuccess {
                    showErrorLiveData.postValue(false)
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelLifecycle.currentState = Lifecycle.State.DESTROYED
    }

    fun showError(): LiveData<Boolean> = showErrorLiveData

    private fun generateNote(): Note {
        return Note()
    }
}