package com.example.mynotesapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.NoteDao
import com.example.mynotesapp.data.NotesDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesDaoTest {
    private lateinit var database: NotesDatabase
    private lateinit var notesDao: NoteDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            androidx.test.core.app.ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()

        notesDao = database.noteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNoteAndRetrieveById() = runBlocking {
        val note = Note(id = 1, title = "Test Note", content = "Content")
        notesDao.insert(note)

        val retrievedNote = notesDao.getNotesById("1")
        assertEquals("Test Note", retrievedNote[note.id].title)
        assertEquals("Content", retrievedNote[note.id].content)
    }

    @Test
    fun deleteNote() = runBlocking {
        val note = Note(id = 1, title = "Test Note", content = "Content")
        notesDao.insert(note)

        notesDao.delete(note)
        val notes = notesDao.getNotesById("1")
        assertTrue(notes[note.id].title.isEmpty())
        assertTrue(notes[note.id].content.isEmpty())
    }
}
