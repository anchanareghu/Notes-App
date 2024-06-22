package com.example.mynotesapp.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mynotesapp.Note
import com.example.mynotesapp.NoteDao


class FakeNoteDao : NoteDao {
    override fun getAllNotes(): List<Note> {
        return listOf(
            Note(
                id = 1,
                title = "Sample Note 1",
                content = "This is a sample note content for preview.",
                color = Color.Red.toArgb(),
                isFavorite = false
            ),
            Note(
                id = 2,
                title = "Sample Note 2",
                content = "Another sample note content for preview.",
                color = Color.Green.toArgb(),
                isFavorite = false
            )
        )
    }

    override fun searchNotes(query: String): List<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun update(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(note: Note) {
        TODO("Not yet implemented")
    }

    // Implement other methods with dummy behavior if needed
}

