package com.example.mynotesapp.components.tasks

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mynotesapp.components.appbars.CustomBottomAppBar
import com.example.mynotesapp.components.imagePickerLauncher
import com.example.mynotesapp.data.Task
import com.example.mynotesapp.data.TaskEntry
import com.example.mynotesapp.firestore.FireStoreNotesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    navController: NavHostController
) {
    val viewModel: FireStoreNotesViewModel = hiltViewModel()

    var title by rememberSaveable { mutableStateOf("") }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    val tasks = remember { mutableStateListOf(TaskEntry()) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val imageUris by rememberSaveable { mutableStateOf(mutableListOf<String>()) }

    val launcher = imagePickerLauncher { uri ->
        imageUris.add(uri.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.Red
                        )
                    }
                }
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                onAddClick = {
                    val newTask = Task(
                        title = title,
                        taskEntries = tasks,
                        isCompleted = isCompleted,
                        isFavorite = isFavorite,
                        imageUris = imageUris
                    )
                    viewModel.insertTask(newTask)
                    navController.popBackStack()
                },
                onImageClick = {
                    launcher.launch("image/*")
                },
                onBoldClick = {},
                onItalicClick = {},
                onUnderlineClick = {}
            )
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TaskTextField(
                    text = title,
                    onTextChange = { title = it },
                    hint = "Task Title",
                    fontSize = 28.sp,
                    imeAction = { tasks.add(TaskEntry()) }
                )

                if (imageUris.isNotEmpty()) {
                    LazyColumn {
                        items(imageUris) { uri ->
                            val inputStream =
                                context.contentResolver.openInputStream(Uri.parse(uri))
                            val bitmap = BitmapFactory.decodeStream(inputStream)

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clickable {
                                        scope.launch {
                                            val result = snackBarHostState.showSnackbar(
                                                message = "Delete this image?",
                                                actionLabel = "Delete"
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                imageUris.remove(uri)
                                            }
                                        }
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    itemsIndexed(tasks) { index, entry ->
                        TaskEditor(
                            text = entry.text,
                            checked = entry.isChecked,
                            onTextChange = { newText ->
                                tasks[index] = entry.copy(text = newText)
                            },
                            onCheckedChange = { isChecked ->
                                tasks[index] = entry.copy(isChecked = isChecked)
                            },
                            onImeAction = {
                                if (index == tasks.size - 1) {
                                    tasks.add(TaskEntry())
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

