package com.example.mynotesapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.Note
import com.example.mynotesapp.NotesViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesEditorScreen(
    navController: NavHostController,
    notesViewModel: NotesViewModel,
    noteId: Int? = null
) {
    val note = noteId?.let { notesViewModel.notes.value?.find { it.id == noteId } }
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var isFavorite by remember {
        mutableStateOf(
            note?.isFavorite ?: false
        )
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            if (note != null) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(note.color)
                    ),
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { navController.popBackStack() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                            }
                        ) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = Color.Red
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        val newNote = Note(
                            id = note?.id ?: 0,
                            title = title,
                            content = content,
                            color = note?.color ?: getRandomColor().toArgb(),
                            isFavorite = isFavorite
                        )
                        if (note == null) notesViewModel.insert(newNote) else notesViewModel.update(
                            newNote
                        )
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save Note")
            }
        },
        content = {
            Surface(
                color = Color(note?.color ?: getRandomColor().toArgb())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    NoteTextField(
                        value = title,
                        onValueChange = { title = it },
                        hint = "Enter title",
                        fontSize = 24.sp
                    )
                    NoteTextField(
                        value = content,
                        onValueChange = { content = it },
                        hint = "Enter content",
                        fontSize = 18.sp,
                    )
                }
            }
        }
    )
}

@Composable
fun NoteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    fontSize: TextUnit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(15.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        singleLine = false,
        textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = LocalTextStyle.current.copy(
                        color = Color.Gray,
                        fontSize = fontSize
                    )
                )
            }
            innerTextField()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NotesEditorScreenPreview() {
    NotesEditorScreen(
        navController = rememberNavController(),
        notesViewModel = NotesViewModel(FakeNoteDao())
    )
}