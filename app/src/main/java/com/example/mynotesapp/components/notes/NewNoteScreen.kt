package com.example.mynotesapp.components.notes

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mynotesapp.components.appbars.CustomBottomAppBar
import com.example.mynotesapp.components.imagePickerLauncher
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.firestore.FireStoreNotesViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(
    navController: NavHostController,
    notesViewModel: FireStoreNotesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    // State for note fields
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    val imageUris by rememberSaveable { mutableStateOf(mutableListOf<String>()) }

    val launcher = imagePickerLauncher { uri ->
        imageUris.add(uri.toString())
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
                onImageClick = { launcher.launch("image/*") },
                onBoldClick = { },
                onItalicClick = { },
                onUnderlineClick = {},
                onAddClick = {
                    val newNote = Note(
                        title = title,
                        content = content,
                        isFavorite = isFavorite,
                        imageUris = imageUris
                    )
                    notesViewModel.insertNote(newNote)
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
                    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())

                Text(
                    text = formattedDate,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp)
                )

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

                // Display added images
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
            }
        }
    )
}
