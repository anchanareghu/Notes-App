package com.example.mynotesapp.firestore


import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    private fun getEmail(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.email ?: ""
    }

    fun saveProfileDetails(userName: String, photoUrl: String) {
        val email = getEmail()
        val userDoc = firestore.collection("users").document(email)

        val profileData = mapOf(
            "username" to userName,
            "photoUrl" to photoUrl
        )

        userDoc.set(profileData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("FireStoreRepo", "Profile details saved successfully")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    fun getProfileDetails(onSuccess: (String, String) -> Unit, onFailure: (Exception) -> Unit) {
        val email = getEmail()
        firestore.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("username") ?: "Guest"
                    val photoUrl = document.getString("photoUrl") ?: ""
                    onSuccess(userName, photoUrl)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Get notes for the logged-in user
    fun getNotes(): Flow<List<Note>> = flow {
        val email = getEmail()
        try {
            val documents = firestore.collection("users")
                .document(email)
                .collection("notes")
                .get()
                .await()

            val notesList = documents.map { document ->
                document.toObject(Note::class.java)
            }
            emit(notesList)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun getTasks(): Flow<List<Task>> = flow {
        val email = getEmail()
        try {
            val documents = firestore.collection("users")
                .document(email)
                .collection("tasks")
                .get()
                .await()

            val tasksList = documents.map { document ->
                document.toObject(Task::class.java)
            }
            emit(tasksList)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    // Insert note into the user's notes collection
    fun insertNote(note: Note) {
        val email = getEmail()
        firestore.collection("users")
            .document(email)
            .collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                note.id = documentReference.id
                documentReference.set(note)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    fun insertTask(task: Task) {
        val email = getEmail()
        firestore.collection("users")
            .document(email)
            .collection("tasks")
            .add(task)
            .addOnSuccessListener { documentReference ->
                task.id = documentReference.id
                documentReference.set(task)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    // Search notes based on query for the logged-in user
    fun searchNotes(
        query: String,
        onSuccess: (List<Note>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("users")
            .document(getEmail())
            .collection("notes")
            .whereGreaterThanOrEqualTo("title", query)
            .get()
            .addOnSuccessListener { result ->
                val notes = result.toObjects(Note::class.java)
                onSuccess(notes)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Add or update a note in the user's notes collection
    suspend fun addOrUpdateNote(note: Note) {
        val email = getEmail()
        try {
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(note.id)
                .set(note)
                .await()
        } catch (e: Exception) {
            // Handle exception
        }
    }

    suspend fun addOrUpdateTask(task: Task) {
        val email = getEmail()
        try {
            firestore.collection("users")
                .document(email)
                .collection("tasks")
                .document(task.id)
                .set(task)
                .await()
        } catch (e: Exception) {
            // Handle exception
        }
    }

    // Delete a note from the user's notes collection
    suspend fun deleteNote(noteId: String) {
        try {
            firestore.collection("users")
                .document(getEmail())
                .collection("notes")
                .document(noteId)
                .delete()
                .await()
        } catch (e: Exception) {
            // Handle exception
        }
    }

    suspend fun deleteTask(taskId: String) {
        try {
            firestore.collection("users")
                .document(getEmail())
                .collection("tasks")
                .document(taskId)
                .delete().await()
        } catch (e: Exception) {
            // Handle exception
        }
    }
}
