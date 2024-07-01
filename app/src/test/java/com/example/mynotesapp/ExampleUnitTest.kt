package com.example.mynotesapp

import org.junit.Test

import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

//
//@ExperimentalCoroutinesApi
//class NotesViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var noteDao: NoteDao
//
//    private lateinit var notesViewModel: NotesViewModel
//
//    private val testDispatcher = TestCoroutineDispatcher()
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        Dispatchers.setMain(testDispatcher)
//        notesViewModel = NotesViewModel(noteDao)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
//    }
//
//    @Test
//    fun insertNote_shouldAddNoteToDatabase() = runBlockingTest {
//        val note = Note(
//            id = 1,
//            title = "Test Note",
//            content = "This is a test note",
//            isFavorite = false,
//            date = System.currentTimeMillis(),
//            imageUri = ""
//        )
//
//        notesViewModel.insert(note)
//
//        verify(noteDao, times(1)).insert(note)
//    }
//
//    @Test
//    fun updateNote_shouldUpdateNoteInDatabase() = runBlockingTest {
//        val note = Note(
//            id = 1,
//            title = "Updated Note",
//            content = "This note has been updated",
//            isFavorite = true,
//            date = System.currentTimeMillis(),
//            imageUri = ""
//        )
//
//        notesViewModel.update(note)
//
//        verify(noteDao, times(1)).update(note)
//    }
//
//    @Test
//    fun deleteNote_shouldRemoveNoteFromDatabase() = runBlockingTest {
//        val note = Note(
//            id = 1,
//            title = "Test Note",
//            content = "This is a test note",
//            isFavorite = false,
//            date = System.currentTimeMillis(),
//            imageUri = ""
//        )
//
//        notesViewModel.delete(note)
//
//        verify(noteDao, times(1)).delete(note)
//    }
//
//    @Test
//    fun getNotes_shouldReturnListOfNotes() = runBlockingTest {
//        val notes = listOf(
//            Note(
//                id = 1,
//                title = "Test Note 1",
//                content = "This is the first test note",
//                isFavorite = false,
//                date = System.currentTimeMillis(),
//                imageUri = ""
//            ),
//            Note(
//                id = 2,
//                title = "Test Note 2",
//                content = "This is the second test note",
//                isFavorite = true,
//                date = System.currentTimeMillis(),
//                imageUri = ""
//            )
//        )
//
//        `when`(noteDao.getNotes()).thenReturn(notes)
//
//        val observer = mock(Observer::class.java) as Observer<List<Note>>
//        notesViewModel.notes.observeForever(observer)
//
//        notesViewModel.notes
//
//        verify(observer).onChanged(notes)
//    }
//}
