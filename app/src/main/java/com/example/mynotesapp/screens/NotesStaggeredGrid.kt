package com.example.mynotesapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotesapp.Note


@Composable
fun NotesGrid(notes: List<Note>, onNoteClick: (Note, Boolean) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        items(notes) { note ->
            NoteCard(
                note = note,
                onClick = { onNoteClick(note, false) },
                onLongClick = { onNoteClick(note, true) }
            )
        }
       }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(Color(note.color))
    ) {
        if (note.isFavorite) {
            Image(
                imageVector = Icons.Default.Favorite,
                contentDescription = "favorite",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
                    .size(18.dp),
                colorFilter = ColorFilter.tint(Color.Red)
            )
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = note.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.padding(8.dp),
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray,
            )
            Text(
                text = note.content,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp),
                maxLines = 8,
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray
            )
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
        color = getRandomColor().toArgb(),
        isFavorite = false
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
                content = "Content",
                color = getRandomColor().toArgb(),
                isFavorite = false
            ),
            Note(
                id = 2,
                title = "Another Title",
                content = "Some more content",
                color = getRandomColor().toArgb(),
                isFavorite = true
            ),
            Note(
                id = 3,
                title = "Lorem Ipsum",
                content = "There are many variations of passages of Lorem Ipsum available, " +
                        "but the majority have suffered alteration in some form, by injected humour," +
                        " or randomised words which don't look even slightly believable.",
                color = getRandomColor().toArgb(),
                isFavorite = true
            )
        )
    ) { _, _ -> }
}

fun getRandomColor(): Color {
    val colors = listOf(
        Color(0xFFe8dff5),
        Color(0xFFfce1e4),
        Color(0xFFfcf4dd),
        Color(0xFFddedea),
        Color(0xFFdaeaf6)
    )
    return colors.random()
}