package com.koshake1.notes

import androidx.multidex.MultiDexApplication
import com.koshake1.notes.di.DependencyGraph
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NotesApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NotesApp)
            modules(DependencyGraph.modules)
        }
    }
}