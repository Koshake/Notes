package com.koshake1.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.koshake1.notes.data.NotesRepository

class MainViewModel(private val notesRepository: NotesRepository) : ViewModel() {
    fun observeViewState(): LiveData<ViewState> = notesRepository.observeNotes()
        .map {
            if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
        }
}