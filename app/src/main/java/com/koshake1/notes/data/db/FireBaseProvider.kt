package com.koshake1.notes.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.User
import com.koshake1.notes.errors.NoAuthException

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"
const val TAG = "FireStoreDatabase"

class FireBaseProvider : DatabaseProvider {
    private val db = FirebaseFirestore.getInstance()
    private val result = MutableLiveData<List<Note>>()
    private var subscribedOnDb = false

    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    override fun observeNotes(): LiveData<List<Note>> {
        if (!subscribedOnDb) subscribeForDbChanging()
        return result
    }

    override fun getCurrentUser(): User? {
        return currentUser?.run {
            User(displayName, email)
        }
    }

    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        val result = MutableLiveData<Result<Note>>()

        getUserNotesCollection()
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

    override fun deleteNote(deletedNote: Note): LiveData<Result<Note>> {
        val result = MutableLiveData<Result<Note>>()
            getUserNotesCollection().document(deletedNote.id.toString()).delete()
                .addOnSuccessListener {
                    result.value = Result.success(deletedNote)
                }.addOnFailureListener {
                    result.value = Result.failure(it)
                }
        return result
    }

    private fun subscribeForDbChanging() {
        getUserNotesCollection().addSnapshotListener { snapshot, e ->
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

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    private inline fun handleNotesReference(
        referenceHandler: (CollectionReference) -> Unit,
        exceptionHandler: (Throwable) -> Unit = {}
    ) {
        kotlin.runCatching {
            getUserNotesCollection()
        }
            .fold(referenceHandler, exceptionHandler)
    }
}