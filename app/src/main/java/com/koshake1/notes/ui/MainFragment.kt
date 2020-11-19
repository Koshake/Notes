package com.koshake1.notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.koshake1.notes.viewmodel.MainViewModel
import com.koshake1.notes.R
import com.koshake1.notes.data.NotesRepositoryImpl.getAllNotes
import com.koshake1.notes.ui.adapter.NotesAdapter
import com.koshake1.notes.viewmodel.ViewState
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(
            MainViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NotesAdapter(getAllNotes())

        mainRecycler.adapter = adapter

        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Value -> {
                }
                ViewState.EMPTY -> Unit
            }
        }
    }
}