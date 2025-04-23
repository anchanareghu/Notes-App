package com.example.mynotesapp.components.notes

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.NotesViewModel
import com.example.mynotesapp.components.appbars.NotesBottomAppBar
import com.example.mynotesapp.components.imagePickerLauncher
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesEditorScreen(
    navController: NavHostController,
) {
    val notesViewModel: NotesViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val noteId = navController.currentBackStackEntry?.arguments?.getString("noteId")
    val note = notesViewModel.notes.value?.find { it.id == noteId?.toInt() }

    note?.let {
        var title by rememberSaveable { mutableStateOf(note.title) }
        var content by rememberSaveable { mutableStateOf(note.content) }
        var isFavorite by rememberSaveable { mutableStateOf(note.isFavorite) }
        var imageUris by rememberSaveable { mutableStateOf(note.imageUris) }

        val launcher = imagePickerLauncher { uri ->
            imageUris = imageUris + uri.toString()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isFavorite = !isFavorite
                            notesViewModel.update(note.copy(isFavorite = isFavorite))
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
                NotesBottomAppBar(
                    onImageClick = { launcher.launch("image/*") },
                    onBoldClick = { },
                    onItalicClick = { },
                    onUnderlineClick = { },
                    onAddClick = {
                        val updatedNote = note.copy(
                            title = title,
                            content = content,
                            isFavorite = isFavorite,
                            imageUris = imageUris
                        )
                        notesViewModel.insert(updatedNote)
                        navController.popBackStack()
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    val formattedDate =
                        SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale.getDefault()
                        ).format(Date(note.date))

                    Text(
                        text = formattedDate,
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp)
                    )

                    if (imageUris.isNotEmpty()) {
                        LazyRow {
                            items(imageUris.size) { index ->
                                val inputStream =
                                    context.contentResolver.openInputStream(Uri.parse(imageUris[index]))
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
                                                    notesViewModel.insert(
                                                        note.copy(
                                                            imageUris = imageUris
                                                        )
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

                    NoteTextField(
                        text = title,
                        onTextChange = { title = it },
                        hint = "Enter title",
                        fontSize = 28.sp,
                    )

                    NoteTextField(
                        text = content,
                        onTextChange = { content = it },
                        hint = "Enter content",
                        fontSize = 24.sp
                    )
                }
            })
    }
}

@Composable
fun NoteTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    fontSize: TextUnit,
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
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    )
}


@Preview(showBackground = true)
@Composable
fun NotesEditorScreenPreview() {
    NotesEditorScreen(
        navController = rememberNavController(),
    )
}