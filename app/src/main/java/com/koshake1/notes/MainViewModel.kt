package com.koshake1.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

fun interface Listener {
    fun handle(string: String)
}

class Model(private val listener: Listener) {
    private fun getData() : String {
        return "Hello world!"
    }

    fun calculateValue() {
        listener.handle(getData())
    }
}
class MainViewModel : ViewModel() {
    private val model  = Model {
        textData.value = it
    }
    private val textData = MutableLiveData("New text")

    fun observeTextData():MutableLiveData<String> = textData

    fun buttonClicked() {
        model.calculateValue()
    }
}