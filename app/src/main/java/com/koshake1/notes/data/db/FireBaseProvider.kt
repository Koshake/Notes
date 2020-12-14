package com.koshake1.notes.data.db

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.koshake1.notes.data.Note
import com.koshake1.notes.data.User
import com.koshake1.notes.errors.NoAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"
const val TAG = "FireStoreDatabase"

class FireBaseProvider(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : DatabaseProvider {
    private val result = MutableStateFlow<List<Note>?>(null)
    private var subscribedOnDb = false

    private val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun observeNotes(): Flow<List<Note>> {
        if (!subscribedOnDb) subscribeForDbChanging()
        return result.filterNotNull()
    }

    override fun getCurrentUser(): User? {
        return currentUser?.run {
            User(displayName, email)
        }
    }

    override suspend fun addOrReplaceNote(newNote: Note) {
        suspendCoroutine<Unit> { continuation ->
            handleNotesReference(
                {
                    getUserNotesCollection()
                        .document(newNote.id.toString())
                        .set(newNote)
                        .addOnSuccessListener {
                            Log.d(TAG, "Note $newNote is saved")
                            continuation.resumeWith(Result.success(Unit))
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Error saving note $newNote, message: ${it.message}")
                            continuation.resumeWithException(it)
                        }

                }, {
                    Log.e(TAG, "Error getting reference note $newNote, message: ${it.message}")
                    continuation.resumeWithException(it)
                }
            )
        }
    }

    override suspend fun deleteNote(deletedNote: Note) {
        suspendCancellableCoroutine<Unit> { continuation ->
            getUserNotesCollection().document(deletedNote.id.toString()).delete()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Unit))
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    private fun subscribeForDbChanging() {
        handleNotesReference(
            {
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
            }, {
                Log.e(TAG, "Error getting reference while subscribed for notes")
            }
        )
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