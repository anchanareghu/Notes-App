package com.example.mynotesapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mynotesapp.data.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesGrid(notes: List<Note>, onNoteClick: (Note, Boolean) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    onClick = { onNoteClick(note, false) },
                    onLongClick = { onNoteClick(note, true) }
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
    val creationDate = note.date
    val formattedDate =
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(creationDate))
    Card(
        modifier = Modifier
            .heightIn(min = 100.dp, max = 300.dp)
            .padding(4.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
                if (note.isFavorite) {
                    Image(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "favorite",
                        modifier = Modifier
                            .size(18.dp),
                        colorFilter = ColorFilter.tint(Color.Red)
                    )
                }
            }
            if (note.imageUri.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(note.imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            if (note.title.isNotEmpty()) {
                Text(
                    text = note.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            if (note.content.isNotEmpty()) {
                Text(
                    text = note.content,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteCardPreview() {
    val note = Note(
        id = 1,
        title = "Title",
        content = "Content",
        isFavorite = false,
        isUnderlined = false,
        isItalic = false,
        isBold = false,
        date = System.currentTimeMillis(),
        imageUri = "https://picsum.photos/200/300"
    )
    NoteCard(note, onClick = {}, onLongClick = {})
}

@Preview(showBackground = true)
@Composable
fun NotesGridPreview() {
    NotesGrid(
        notes = listOf(
            Note(
                id = 1,
                title = "Title",
                imageUri = "https://picsum.photos/200/300",
                content = "Content",
                isFavorite = false,
                isUnderlined = false,
                isItalic = false,
                isBold = false,
                date = System.currentTimeMillis()
            ),
            Note(
                id = 2,
                title = "Another Title",
                imageUri = "",
                content = "Some more content",
                isFavorite = true,
                isUnderlined = true,
                isItalic = true,
                isBold = true,
                date = System.currentTimeMillis()
            ),
            Note(
                id = 3,
                title = "Lorem Ipsum",
                imageUri = "",
                content = "There are many variations of passages of Lorem Ipsum available, " +
                        "but the majority have suffered alteration in some form, by injected humour," +
                        " or randomised words which don't look even slightly believable.",
                isFavorite = true,
                isUnderlined = true,
                isItalic = true,
                isBold = true,
                date = System.currentTimeMillis()
            ),
            Note(
                id = 4,
                title = "Lorem Ipsum",
                imageUri = "",
                content = "the majority have suffered alteration in some form, by injected humour," +
                        " or randomised words which don't look even slightly believable.",
                isFavorite = true,
                isUnderlined = true,
                isItalic = true,
                isBold = true,
                date = System.currentTimeMillis()
            )
        )
    ) { _, _ -> }
}