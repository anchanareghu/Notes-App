package com.example.mynotesapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mynotesapp.Note
import com.example.mynotesapp.NotesViewModel
import com.example.mynotesapp.screens.appbars.DeletionConfirmationDialog
import com.example.mynotesapp.screens.appbars.SearchBar

@Composable
fun MainScreen(navController: NavHostController, notesViewModel: NotesViewModel) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val notes by notesViewModel.notes.observeAsState(initial = emptyList())


    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        content = {
            Column(modifier = Modifier.padding(it)) {
                SearchBar(searchQuery) { textFieldValue ->
                    searchQuery = textFieldValue
                    notesViewModel.searchNotes(textFieldValue.text)
                }
                Spacer(modifier = Modifier.padding(8.dp))
                NotesGrid(notes) { note, longClick ->
                    if (longClick) {
                        noteToDelete = note
                        showDialog = true
                    } else {
                        navController.navigate("note_detail/${note.id}")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = { navController.navigate("new_note") },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Add Note")
            }
        }
    )

    if (showDialog) {
        noteToDelete?.let { note ->
            DeletionConfirmationDialog(
                onDeleteConfirmed = {
                    notesViewModel.delete(note)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = NavHostController(LocalContext.current),
        notesViewModel = NotesViewModel(FakeNoteDao()),
    )
}