package com.example.mynotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mynotesapp.data.NotesDatabase
import com.example.mynotesapp.components.appbars.NotesAppNavHost
import com.example.mynotesapp.ui.theme.MyNotesAppTheme
import com.example.mynotesapp.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    private lateinit var notesDatabase: NotesDatabase
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesDatabase = NotesDatabase.getDatabase(this)

        setContent {
            MyNotesAppTheme {
                notesViewModel = NotesViewModel(
                    notesDatabase.noteDao()
                )
                NotesAppNavHost(notesViewModel = notesViewModel)
            }
        }
    }
}