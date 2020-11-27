package com.koshake1.notes.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.koshake1.notes.data.Note

private const val NOTES_COLLECTION = "notes"
const val TAG = "FireStoreDatabase"

class FireBaseProvider : DatabaseProvider {
    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)
    private val result = MutableLiveData<List<Note>>()
    private var subscribedOnDb = false

    override fun observeNotes(): LiveData<List<Note>> {
        if (!subscribedOnDb) subscribeForDbChanging()
        return result
    }

    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        val result = MutableLiveData<Result<Note>>()

        notesReference
            .document(newNote.id.toString())
            .set(newNote)
            .addOnSuccessListener {
                Log.d(TAG, "Note $newNote is saved")
                result.value = Result.success(newNote)
            }
            .addOnFailureListener {
                Log.d(TAG, "Error saving note $newNote, message: ${it.message}")
                result.value = Result.failure(it)
            }

        return result
    }

    private fun subscribeForDbChanging() {
        notesReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e(TAG, "Observe note exception:$e")
            } else if (snapshot != null) {
                val notes = mutableListOf<Note>()

                for (doc: QueryDocumentSnapshot in snapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }

                result.value = notes
            }
        }

        subscribedOnDb = true
    }
}