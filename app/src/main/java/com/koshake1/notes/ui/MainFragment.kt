package com.koshake1.notes.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.koshake1.notes.viewmodel.MainViewModel
import com.koshake1.notes.R
import com.koshake1.notes.data.Note
import com.koshake1.notes.ui.adapter.NotesAdapter
import com.koshake1.notes.viewmodel.ViewState
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.toolbar
import kotlinx.android.synthetic.main.fragment_note.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(
            MainViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)

        val adapter = NotesAdapter {
            navigateToNote(it)
        }

        mainRecycler.adapter = adapter

        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Value -> {
                    adapter.submitList(it.notes)
                }
                ViewState.EMPTY -> Unit
            }
        }

        fab.setOnClickListener {
            navigateToCreation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> showLogoutDialog().let { true }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() {
        fragmentManager?.findFragmentByTag(LogoutDialog.TAG) ?: fragmentManager?.let {
            LogoutDialog.createInstance().show(
                it, LogoutDialog.TAG
            )
        }
    }

    private fun navigateToNote(note: Note) {
        (requireActivity() as MainActivity).navigateTo(NoteFragment.create(note))
    }

    private fun navigateToCreation() {
        (requireActivity() as MainActivity).navigateTo(NoteFragment.create(null))
    }
}