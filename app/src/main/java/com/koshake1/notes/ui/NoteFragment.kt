package com.koshake1.notes.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koshake1.notes.R
import com.koshake1.notes.data.Note
import com.koshake1.notes.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note.*


class NoteFragment : Fragment(R.layout.fragment_note) {
    private val note: Note? by lazy(LazyThreadSafetyMode.NONE) { arguments?.getParcelable(NOTE_KEY) }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NoteViewModel(note) as T
            }
        }).get(
            NoteViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)

        viewModel.note?.let {
            titleEt.setText(it.title)
            bodyEt.setText(it.note)
        }

        viewModel.showError().observe(viewLifecycleOwner) {
            val text = if (it) {
                "Error while saving note!"
            } else {
                "Note is saved!"
            }
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }

        titleEt.addTextChangedListener {
            viewModel.updateTitle(it?.toString() ?: "")
        }

        bodyEt.addTextChangedListener {
            viewModel.updateNote(it?.toString() ?: "")
        }

        buttonSave.setOnClickListener {
            viewModel.saveNote()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {
                view?.clearFocus()
                activity?.onBackPressed()
                return true
            }
            R.id.action_save -> {
                view?.clearFocus()
                viewModel.saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val NOTE_KEY = "Note"

        fun create(note: Note? = null): NoteFragment {
            return NoteFragment().also {
                val arguments = Bundle()
                arguments.putParcelable(NOTE_KEY, note)
                it.arguments = arguments
            }
        }
    }
}