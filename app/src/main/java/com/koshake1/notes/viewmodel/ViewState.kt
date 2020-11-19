package com.koshake1.notes.viewmodel

import com.koshake1.notes.data.Note

sealed class ViewState {
    data class Value(val notes: List<Note>) : ViewState()
    object EMPTY : ViewState()
}