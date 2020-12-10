package com.koshake1.notes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.NotesRepository
import com.koshake1.notes.data.NotesRepositoryImpl
import com.koshake1.notes.data.db.DatabaseProvider
import com.koshake1.notes.data.db.FireBaseProvider
import com.koshake1.notes.viewmodel.MainViewModel
import com.koshake1.notes.viewmodel.NoteViewModel
import com.koshake1.notes.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

object DependencyGraph {
    private val repositoryModule by lazy {
        module {
            single { FirebaseAuth.getInstance() }
            single { FirebaseFirestore.getInstance() }
            single { NotesRepositoryImpl(get()) } bind NotesRepository::class
            single { FireBaseProvider(get(), get()) } bind DatabaseProvider::class
        }
    }

    private val viewModelModule by lazy {
        module {
            viewModel { MainViewModel(get()) }
            viewModel { SplashViewModel(get()) }
            viewModel { (note: Note?) -> NoteViewModel(get(), note) }
        }
    }

    val modules: List<Module> = listOf(repositoryModule, viewModelModule)
}