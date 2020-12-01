package com.koshake1.notes.liveevent

import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class LiveEvent<T> : MutableLiveData<T>() {

    private val observers = ArraySet<EventData<in T>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val observer = EventData(observer)
        observers.add(observer)

        super.observe(owner, observer)
    }

    override fun removeObserver(observer: Observer<in T>) {

        when (observer) {
            is EventData -> {
                observers.remove(observer)
                super.removeObserver(observer)
            }
            else -> {
                val pendingObserver = observers.firstOrNull { it.wrappedObserver == observer }
                if (pendingObserver != null) {
                    observers.remove(pendingObserver)
                    super.removeObserver(pendingObserver)
                }
            }
        }
    }

    override fun setValue(event: T?) {
        observers.forEach { it.awaitValue() }
        super.setValue(event)
    }
}

internal class EventData<T>(val wrappedObserver: Observer<in T>) :
    Observer<T> {
    private var pending = false

    override fun onChanged(event: T?) {
        if (pending) {
            pending = false
            wrappedObserver.onChanged(event)
        }
    }

    fun awaitValue() {
        pending = true
    }
}