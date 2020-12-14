package com.koshake1.notes.viewmodel

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.NoteColor
import com.koshake1.notes.data.NotesRepository
import com.koshake1.notes.data.User
import io.mockk.*
import org.junit.*

import org.junit.Assert.*
import org.junit.rules.TestRule
import kotlin.random.Random

class NoteViewModelTest {

    private val notesRepositoryMock = mockk<NotesRepository>()
    private lateinit var viewModel: NoteViewModel
    private var _resultLiveData = MutableLiveData(
        Result.success(
            Note()
        )
    )
    private val resultLiveData: LiveData<Result<Note>> get() = _resultLiveData

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        every { notesRepositoryMock.addOrReplaceNote(any()) } returns resultLiveData
        // Почему-то не рабатает
        //mockkStatic("android.graphics.Color")
        //every { Color.argb(any<Int>(), any<Int>(), any<Int>(), any<Int>())
        //} returns 0xffffff
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `NotesRepository addOrReplaceNote called with correct title Note`() {
        viewModel = NoteViewModel(notesRepositoryMock, null)

        viewModel.updateNote("New title!")
        viewModel.saveNote()

        verify(exactly = 1) {
            notesRepositoryMock.addOrReplaceNote(match { it.note == "New title!" })
        }
    }

    @Test
    fun `ViewModel Note title changed`() {
        val currentNote = Note(title = "Title!")
        viewModel = NoteViewModel(notesRepositoryMock, currentNote)
        viewModel.updateTitle("New")

        Assert.assertEquals("New", viewModel.note?.title)
    }

    @Test
    fun `ViewModel Note color changed`() {
        val currentNote = Note(color = 0x000000)
        viewModel = NoteViewModel(notesRepositoryMock, currentNote)
        viewModel.updateColor(0xffffff)

        Assert.assertEquals(0xffffff, viewModel.note?.color)
    }

    @Test
    fun `Error LiveData contains nothing after save`() {
        val currentNote = Note(title = "Hello!")
        viewModel = NoteViewModel(notesRepositoryMock, currentNote)
        viewModel.saveNote()

        Assert.assertEquals(viewModel.showError().value, "Successfully saved!")
    }

    @Test
    fun `Error LiveData contains value after save`() {
        every { notesRepositoryMock.addOrReplaceNote(any()) } returns MutableLiveData(
            Result.failure(
                IllegalAccessError()
            )
        )

        val currentNote = Note(title = "Hello!")
        viewModel = NoteViewModel(notesRepositoryMock, currentNote)
        viewModel.saveNote()

        Assert.assertEquals(viewModel.showError().value, "Error while saving")
    }
}