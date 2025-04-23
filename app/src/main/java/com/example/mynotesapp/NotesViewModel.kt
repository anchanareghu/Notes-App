package com.example.mynotesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.NoteDao
import com.example.mynotesapp.data.Task
import com.example.mynotesapp.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteDao: NoteDao,
    private val taskDao: TaskDao
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private var job: Job? = null
    private var taskJob: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {

        getNotes()
        getTasks()
    }
    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    private fun getNotes() {
        job = noteDao.getNotes().onEach { notes ->
            _notes.value = notes
        }.launchIn(viewModelScope)
    }

    private fun getTasks() {
        taskJob?.cancel()
        taskJob = taskDao.getTasks().onEach { tasks ->
            _tasks.value = tasks
        }.launchIn(viewModelScope)
    }

    fun searchNotes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = noteDao.searchNotes("%$query%")
            _notes.value = notes
            noteDao.getNotes()
        }
    }


    fun searchTasks(query: String) {
        viewModelScope.launch (Dispatchers.IO){
            val tasks = taskDao.searchTasks("%$query%")
            _tasks.value= tasks
            taskDao.getTasks()

        }
    }

    fun insert(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun insert(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun update(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }

    }
}