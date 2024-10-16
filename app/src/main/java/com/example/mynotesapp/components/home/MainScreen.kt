package com.example.mynotesapp.components.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mynotesapp.auth.AuthenticationManager
import com.example.mynotesapp.components.appbars.DeletionConfirmationDialog
import com.example.mynotesapp.components.appbars.SearchBar
import com.example.mynotesapp.components.fab.FABViewModel
import com.example.mynotesapp.components.notes.NotesGrid
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.Task
import com.example.mynotesapp.firestore.FireStoreNotesViewModel

@Composable
fun MainScreen(
    navController: NavHostController
) {
    val notesViewModel: FireStoreNotesViewModel = hiltViewModel()
    val fabViewModel: FABViewModel = hiltViewModel()
    val authManager = AuthenticationManager(LocalContext.current)
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val notes =
        if (searchQuery.text.isNotEmpty()) notesViewModel.searchResults else notesViewModel.notesState

    val tasks = notesViewModel.tasksState

    val isLoading = notesViewModel.isLoading
    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    val expandedFabState = remember {
        derivedStateOf {
            notes.size <= 3
        }
    }

    LaunchedEffect(key1 = expandedFabState.value) {
        fabViewModel.expandedFab.value = expandedFabState.value
    }

    LaunchedEffect(key1 = Unit) {
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
                    .padding(horizontal = 8.dp), // Add padding to the row
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Search bar with weight to fill remaining space
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    SearchBar(searchQuery) {
                        searchQuery = it
                        notesViewModel.searchNotes(searchQuery.text)
                    }
                }

                // Profile image with fixed size
                if (authManager.auth.currentUser != null) {
                    AsyncImage(
                        model = authManager.auth.currentUser?.photoUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 2.dp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .clickable { navController.navigate("profile") }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp) // Fixed size for default icon
                            .border(
                                width = 2.dp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .clickable { navController.navigate("profile") }
                    )
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
                    isLoading -> {
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
                                    navController.navigate("edit_task/${task.id}")
                                    showDialog = false
                                }
                            },
                            onNoteClick = { note, isLongClick ->
                                if (isLongClick) {
                                    noteToDelete = note
                                    showDialog = true
                                } else {
                                    navController.navigate("edit_note/${note.id}")
                                }
                            })
                    }
                }
            }
            if (showDialog) {
                DeletionConfirmationDialog(
                    onDismiss = { showDialog = false },
                    onDelete = {
                        noteToDelete?.let { notesViewModel.delete(it) }
                        taskToDelete?.let { notesViewModel.deleteTask(it) }
                        showDialog = false
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
                    onClick = {
                        fabViewModel.smallFabOnClick.value.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Add Task",
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExtendedFloatingActionButton(
                    text = {
                        Text(text = "Add Note")
                    },
                    expanded = fabViewModel.expandedFab.value,
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        fabViewModel.fabOnClick.value.invoke()
                    },
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

@Composable
fun ProfileImage(
    imageUrl: Uri?, onImageChangeClick: (newUri: Uri) -> Unit = {},
    isEditing: Boolean
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onImageChangeClick(it)
        }
    }

    Box(Modifier.height(140.dp)) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(3.dp, if (isSystemInDarkTheme()) Color.White else Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(image = Icons.Default.AccountCircle),
                error = rememberVectorPainter(image = Icons.Default.AccountCircle),
                contentDescription = null,
            )
        }
        if (isEditing) {
            IconButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .size(35.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = rememberNavController(),
    )
}

