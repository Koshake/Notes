package com.koshake1.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java)}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeTextData().observe(viewLifecycleOwner) { data: String? ->
            textView.text = data
        }

        buttonPush.setOnClickListener {
            viewModel.buttonClicked()
        }
    }
}