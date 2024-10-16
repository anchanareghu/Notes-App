package com.example.mynotesapp.components.tasks

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mynotesapp.components.appbars.CustomBottomAppBar
import com.example.mynotesapp.components.imagePickerLauncher
import com.example.mynotesapp.data.TaskEntry
import com.example.mynotesapp.firestore.FireStoreNotesViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    navController: NavHostController,
) {
    val notesViewModel: FireStoreNotesViewModel = hiltViewModel()

    val taskId = navController.currentBackStackEntry?.arguments?.getString("taskId")
    val task = notesViewModel.tasksState.find { it.id == taskId }
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    task?.let {
        var title by rememberSaveable { mutableStateOf(task.title) }
        var content by rememberSaveable { mutableStateOf(task.taskEntries) }
        var isCompleted by rememberSaveable { mutableStateOf(task.isCompleted) }
        var imageUris by rememberSaveable { mutableStateOf(task.imageUris) }
        var isFavorite by rememberSaveable { mutableStateOf(task.isFavorite) }

        val tasks = remember {
            content.map { it.copy() }.toMutableStateList()
        }


        val launcher = imagePickerLauncher { uri ->
            imageUris = imageUris + uri.toString()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isFavorite = !isFavorite
                            notesViewModel.addOrUpdateTask(task.copy(isFavorite = isFavorite))
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
                        val updatedTask = task.copy(
                            title = title,
                            taskEntries = tasks.toList(), // Store the list of TaskEntry
                            isCompleted = isCompleted,
                            isFavorite = isFavorite,
                            imageUris = imageUris
                        )

                        notesViewModel.addOrUpdateTask(updatedTask)
                        navController.popBackStack()
                    },
                    onBoldClick = { },
                    onItalicClick = { },
                    onUnderlineClick = { },
                    onImageClick = { launcher.launch("image/*") }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val formattedDate =
                    SimpleDateFormat(
                        "dd MMMM yyyy",
                        Locale.getDefault()
                    ).format(Date(task.date))

                Text(
                    text = formattedDate,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp)
                )

                // Render the task editors and add new entries dynamically
                TaskTextField(
                    text = title,
                    onTextChange = { title = it },
                    hint = "Task Title",
                    fontSize = 28.sp,
                    imeAction = { tasks.add(TaskEntry()) } // Add new task entry on Enter
                )
                if (imageUris.isNotEmpty()) {
                    LazyRow {
                        items(imageUris.size) { index ->
                            val uri = Uri.parse(imageUris[index])
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(5.dp)
                                    .clickable {
                                        scope.launch {
                                            val result = snackBarHostState.showSnackbar(
                                                message = "Do you want to delete this image?",
                                                actionLabel = "Delete"
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                imageUris =
                                                    imageUris.filterIndexed { i, _ -> i != index }
                                                notesViewModel.addOrUpdateTask(
                                                    task.copy(imageUris = imageUris)
                                                )
                                            }
                                            inputStream?.close()
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
    }
}


@Composable
fun TaskTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    fontSize: TextUnit,
    imeAction: () -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .padding(15.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        textStyle = TextStyle(
            fontSize = fontSize,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        ),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    text = hint,
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = fontSize,
                        color = Color.Gray
                    )
                )
            }
            innerTextField()
        },
        keyboardActions = KeyboardActions(onDone = {
            imeAction()
        }),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun TaskEditor(
    text: String,
    checked: Boolean,
    onTextChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onImeAction: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                uncheckedColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
            ),
            modifier = Modifier.padding(end = 8.dp)
        )
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onImeAction() }),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            ),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = "Enter task details",
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )
    }
}
