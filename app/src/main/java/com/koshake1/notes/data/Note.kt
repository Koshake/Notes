package com.koshake1.notes.data

data class Note(
    val title: String,
    val note: String,
    val color: Int = 0x000000,
)