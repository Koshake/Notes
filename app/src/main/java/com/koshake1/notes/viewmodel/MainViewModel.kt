package com.koshake1.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koshake1.notes.data.NotesRepositoryImpl

class MainViewModel : ViewModel() {
    private val viewStateLiveData = MutableLiveData<ViewState>(ViewState.EMPTY)

    init {
        val notes = NotesRepositoryImpl.getAllNotes()
        viewStateLiveData.value = ViewState.Value(notes)
    }

    fun observeViewState(): LiveData<ViewState> = viewStateLiveData
}