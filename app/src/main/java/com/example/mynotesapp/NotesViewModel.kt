package com.example.mynotesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(private val noteDao: NoteDao) :
    ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(noteDao.getAllNotes())
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(noteDao.searchNotes("%$query%"))
        }
    }

    fun insert(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insert(note)
            loadNotes()
        }
    }


    fun update(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.update(note)
            loadNotes()
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
            loadNotes()
        }
    }
}




