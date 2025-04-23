package com.example.mynotesapp.data

import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    suspend fun insertNote(note: Note) = noteDao.insert(note)
    suspend fun getNotesById(id: String) = noteDao.getNotesById(id)
}