package com.koshake1.notes.ui

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.koshake1.notes.R
import com.koshake1.notes.data.NoteColor

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.addToBackStack("notes")
    fragmentTransaction.commit()
}

fun NoteColor.getColorInt(context: Context): Int =
    ContextCompat.getColor(context, getColorRes())

fun NoteColor.getColorRes(): Int = when (this) {
    NoteColor.WHITE -> R.color.color_white
    NoteColor.VIOLET -> R.color.color_violet
    NoteColor.YELLOW -> R.color.color_yellow
    NoteColor.RED -> R.color.color_red
    NoteColor.PINK -> R.color.color_pink
    NoteColor.GREEN -> R.color.color_green
    NoteColor.BLUE -> R.color.color_blue
    NoteColor.INDIAN_RED-> R.color.color_indian_red
    NoteColor.GRAY -> R.color.color_gray
    NoteColor.DARK_GREEN -> R.color.color_dark_green
    NoteColor.ORANGE -> R.color.color_orange
}

