package com.example.mynotesapp.components.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mynotesapp.NotesViewModel
import com.example.mynotesapp.components.appbars.DeletionConfirmationDialog
import com.example.mynotesapp.components.appbars.SearchBar
import com.example.mynotesapp.components.fab.FABViewModel
import com.example.mynotesapp.components.notes.NotesGrid
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.Task
import kotlinx.coroutines.launch
@Composable
fun MainScreen(
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val notesViewModel: NotesViewModel = hiltViewModel()
    val fabViewModel: FABViewModel = hiltViewModel()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val notes by notesViewModel.notes.collectAsState()
    val tasks by notesViewModel.tasks.collectAsState()
    val isLoading = notesViewModel.isLoading.value

    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    val expandedFabState by remember {
        derivedStateOf {
            notes.size <= 3 && tasks.size <= 3
        }
    }

    LaunchedEffect(expandedFabState) {
        fabViewModel.expandedFab.value = expandedFabState
    }

    LaunchedEffect(Unit) {
        fabViewModel.fabOnClick.value = {
            navController.navigate("new_note")
        }

        fabViewModel.smallFabOnClick.value = {
            navController.navigate("new_task")
        }
    }

    Scaffold(
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SearchBar(searchQuery) {
                        searchQuery = it
                        notesViewModel.searchNotes(searchQuery.text)
                        notesViewModel.searchTasks(searchQuery.text)
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading == true -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }

                    notes.isEmpty() && tasks.isEmpty() -> {
                        Text(
                            text = "No notes or tasks available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentHeight()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        NotesGrid(
                            notes = notes,
                            tasks = tasks,
                            onTaskClick = { task, isLongClick ->
                                if (isLongClick) {
                                    taskToDelete = task
                                    showDialog = true
                                } else {
                                    coroutineScope.launch {
                                        notesViewModel.setLoading(true)
                                        navController.navigate("edit_task/${task.id}")
                                        notesViewModel.setLoading(false)
                                    }
                                    showDialog = false
                                }
                            },
                            onNoteClick = { note, isLongClick ->
                                if (isLongClick) {
                                    noteToDelete = note
                                    showDialog = true
                                } else {
                                    coroutineScope.launch {
                                        notesViewModel.setLoading(true)
                                        navController.navigate("edit_note/${note.id}")
                                        notesViewModel.setLoading(false)
                                    }
                                    showDialog = false
                                }
                            }
                        )
                    }
                }
            }

            if (showDialog) {
                DeletionConfirmationDialog(
                    onDismiss = { showDialog = false },
                    onDelete = {
                        coroutineScope.launch {
                            noteToDelete?.let { notesViewModel.delete(it) }
                            taskToDelete?.let { notesViewModel.delete(it) }
                            showDialog = false
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                SmallFloatingActionButton(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    shape = RoundedCornerShape(12.dp),
                    onClick = { fabViewModel.smallFabOnClick.value.invoke() }
                ) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "Add Task")
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExtendedFloatingActionButton(
                    text = { Text(text = "Add Note") },
                    expanded = fabViewModel.expandedFab.value,
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    shape = RoundedCornerShape(12.dp),
                    onClick = { fabViewModel.fabOnClick.value.invoke() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Note",
                            tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        )
                    },
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = rememberNavController(),
    )
}

