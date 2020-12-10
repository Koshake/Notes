package com.koshake1.notes.data

import android.graphics.Color
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

private val idRandom = Random(0)
val noteId: Long
    get() = idRandom.nextLong()
val noteColor: Int
    get() = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

@Parcelize
data class Note(
    val id: Long = noteId,
    val title: String = "",
    val note: String = "",
    val color: Int = noteColor,
) : Parcelable

enum class NoteColor {
    WHITE,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK,
    INDIAN_RED,
    GRAY,
    DARK_GREEN,
    ORANGE;
}