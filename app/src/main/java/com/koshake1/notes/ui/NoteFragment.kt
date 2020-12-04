package com.koshake1.notes.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.koshake1.notes.R
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.NoteColor
import com.koshake1.notes.databinding.FragmentNoteBinding
import com.koshake1.notes.viewmodel.NoteViewModel
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteFragment : Fragment() {
    private val note: Note? by lazy(LazyThreadSafetyMode.NONE) { arguments?.getParcelable(NOTE_KEY) }

    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding get() = _binding!!

    private val viewModel by viewModel<NoteViewModel> {
        parametersOf(note)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        with(binding) {
            viewModel.note?.let {
                titleEt.setText(it.title)
                bodyEt.setText(it.note)
            }

            (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
            titleEt.addTextChangedListener {
                viewModel.updateTitle(it?.toString() ?: "")
            }

            bodyEt.addTextChangedListener {
                viewModel.updateNote(it?.toString() ?: "")
            }

            buttonSave.setOnClickListener {
                viewModel.saveNote()
            }

            colorPicker.onColorClickListener = {
                viewModel.updateColor(it.getColorInt(requireContext()))
                setToolbarColor(it)
            }
        }

        viewModel.showError().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            R.id.action_delete -> {
                viewModel.deleteNote()
                activity?.onBackPressed()
                return true
            }
            R.id.action_palette -> {
                togglePalette()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbarColor(color: NoteColor) {
        context?.let { color.getColorInt(it) }?.let { binding.toolbar.setBackgroundColor(it) }
    }

    private fun togglePalette() {
        if (binding.colorPicker.isOpen) {
            binding.colorPicker.close()
        } else {
            binding.colorPicker.open()
        }
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