package com.example.mynotesapp.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.NotesViewModel
import com.example.mynotesapp.R
import com.example.mynotesapp.data.FakeNoteDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesEditorScreen(
    navController: NavHostController,
    notesViewModel: NotesViewModel = viewModel(),
    noteId: Int? = null
) {
    val context = LocalContext.current
    val note = noteId?.let { notesViewModel.notes.value?.find { it.id == noteId } }
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(note?.content ?: "") }
    var isFavorite by rememberSaveable { mutableStateOf(note?.isFavorite ?: false) }
    var imageUri by rememberSaveable { mutableStateOf(note?.imageUri ?: "") }
    val creationDate = note?.date ?: System.currentTimeMillis()
    val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(creationDate))
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var isBold by rememberSaveable { mutableStateOf(false) }
    var isItalic by rememberSaveable { mutableStateOf(false) }
    var isUnderlined by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (note != null) {
                        IconButton(onClick = { isFavorite = !isFavorite }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = Color.Red
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                onImageClick = { launcher.launch("image/*") },
                onBoldClick = { isBold = !isBold },
                onItalicClick = { isItalic = !isItalic },
                onUnderlineClick = { isUnderlined = !isUnderlined },
                onAddClick = {
                    scope.launch {
                        val newNote = Note(
                            id = note?.id ?: 0,
                            title = title,
                            content = content,
                            isFavorite = isFavorite,
                            date = creationDate,
                            imageUri = imageUri
                        )
                        if (note == null) {
                            notesViewModel.insert(newNote)
                        } else {
                            notesViewModel.update(newNote)
                        }
                        navController.popBackStack()
                    }
                },
                isBold = isBold,
                isItalic = isItalic,
                isUnderline = isUnderlined
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
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
                    isBold = false,
                    isItalic = false,
                    isUnderline = false,
                )
                NoteTextField(
                    text = content,
                    onTextChange = { content = it },
                    hint = "Enter content",
                    fontSize = 24.sp,
                    isBold = isBold,
                    isItalic = isItalic,
                    isUnderline = isUnderlined
                )

                if (imageUri.isNotEmpty()) {
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(imageUri))
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
                                        message = "Are you sure you want to delete this image?",
                                        actionLabel = "Delete"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        imageUri = ""
                                    }
                                }
                            },
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    )
}

@Composable
fun NoteTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    fontSize: TextUnit,
    isBold: Boolean,
    isItalic: Boolean,
    isUnderline: Boolean,
) {
    val textStyle = TextStyle(
        fontSize = fontSize,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
        textDecoration = if (isUnderline) TextDecoration.Underline else TextDecoration.None,
    )

    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .padding(15.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        textStyle = textStyle.copy(
            color = if (text.isEmpty()) Color.Gray else MaterialTheme.colorScheme.onSurface
        ),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    text = hint,
                    color = Color.Gray,
                    style = textStyle.copy(fontSize = fontSize)
                )
            }
            innerTextField()
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun CustomBottomAppBar(
    onImageClick: () -> Unit,
    onAddClick: () -> Unit,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderlineClick: () -> Unit,
    isBold: Boolean,
    isItalic: Boolean,
    isUnderline: Boolean,
) {
    BottomAppBar(
        actions = {
            Row {
                IconButton(onClick = onImageClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_image_24),
                        contentDescription = "Add Image",
                    )
                }
                IconButton(
                    onClick = onBoldClick,
                    modifier = Modifier.background(if (isBold) MaterialTheme.colorScheme.surfaceContainerLow else Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_bold_24),
                        contentDescription = "Bold Text",
                    )
                }
                IconButton(
                    onClick = onItalicClick,
                    modifier = Modifier.background(if (isItalic) MaterialTheme.colorScheme.surfaceContainerLow else Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_italic_24),
                        contentDescription = "Italic Text",
                    )
                }
                IconButton(
                    onClick = onUnderlineClick,
                    modifier = Modifier.background(if (isUnderline) MaterialTheme.colorScheme.surfaceContainerLow else Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_underlined_24),
                        contentDescription = "Underline Text",
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Done, contentDescription = "Update")
            }
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
