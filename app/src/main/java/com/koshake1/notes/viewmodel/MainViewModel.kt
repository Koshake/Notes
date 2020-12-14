package com.koshake1.notes.viewmodel

import androidx.lifecycle.*
import com.koshake1.notes.data.NotesRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(notesRepository: NotesRepository) : ViewModel() {
    private val notesLiveData = MutableLiveData<ViewState>()

    init {
        notesRepository.observeNotes()
            .onEach {
                notesLiveData.value = if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
            }
            .launchIn(viewModelScope)
    }

    fun observeViewState(): LiveData<ViewState> = notesLiveData
}