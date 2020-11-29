package com.koshake1.notes.data

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.koshake1.notes.R
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
