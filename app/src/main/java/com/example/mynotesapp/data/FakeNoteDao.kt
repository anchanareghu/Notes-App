package com.example.mynotesapp.data


class FakeNoteDao : NoteDao {
    override fun getNotes(): List<Note> {
        return listOf(
            Note(
                id = 1,
                title = "Sample Note 1",
                imageUri = "https://example.com/image1.jpg",
                content = "This is a sample note content for preview.",
                isFavorite = false,
                isItalic = false,
                isBold = false,
                isUnderlined = false,
                date = System.currentTimeMillis()
            ),
            Note(
                id = 2,
                title = "Sample Note 2",
                imageUri = "https://example.com/image2.jpg",
                content = "Another sample note content for preview.",
                isFavorite = false,
                isItalic = false,
                isBold = false,
                isUnderlined = false,
                date = System.currentTimeMillis()
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

