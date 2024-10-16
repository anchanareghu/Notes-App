package com.example.mynotesapp.firestore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireStoreNotesViewModel @Inject constructor(
    private val firestoreRepo: FireStoreRepo
) : ViewModel() {

    var notesState by mutableStateOf<List<Note>>(emptyList())
    var tasksState by mutableStateOf<List<Task>>(emptyList())

    var searchResults by mutableStateOf<List<Note>>(emptyList())
    var isLoading by mutableStateOf(false)
    private var errorMessage by mutableStateOf("")

    init {
        loadNotes()
        loadTasks()
    }

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> get() = _note

    private fun loadNotes() {
        isLoading = true
        viewModelScope.launch {
            firestoreRepo.getNotes().collect { notesList ->
                notesState = notesList
                isLoading = false
            }
        }
    }

    private fun loadTasks() {
        isLoading = true
        viewModelScope.launch {
            firestoreRepo.getTasks().collect { tasksList ->
                tasksState = tasksList
                isLoading = false
            }
        }
    }

    fun addOrUpdateNote(note: Note) {
        viewModelScope.launch {
            firestoreRepo.addOrUpdateNote(note)
            loadNotes()
            loadTasks()
        }
    }
    fun addOrUpdateTask(task: Task) {
        viewModelScope.launch {
            firestoreRepo.addOrUpdateTask(task)
            loadTasks()
            loadNotes()
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            firestoreRepo.insertNote(note)
            loadNotes()
        }
    }
    fun insertTask(task: Task){
        viewModelScope.launch {
            firestoreRepo.insertTask(task)
            loadTasks()
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            isLoading = true
            firestoreRepo.searchNotes(
                query = query,
                onSuccess = { notes ->
                    searchResults = notes
                    isLoading = false
                    errorMessage = ""
                },
                onFailure = { exception ->
                    errorMessage = exception.message ?: "An error occurred"
                    isLoading = false
                    searchResults = emptyList()
                }
            )
            loadNotes()
            isLoading = false
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            firestoreRepo.deleteNote(note.id)
            loadNotes()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            firestoreRepo.deleteTask(task.id)
            loadTasks()
        }
    }
}
