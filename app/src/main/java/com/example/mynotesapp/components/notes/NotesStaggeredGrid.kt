package com.example.mynotesapp.components.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotesapp.components.tasks.TaskCard
import com.example.mynotesapp.data.Note
import com.example.mynotesapp.data.Task

@Composable
fun NotesGrid(
    notes: List<Note>,
    tasks: List<Task>,
    onNoteClick: (Note, Boolean) -> Unit,
    onTaskClick: (Task, Boolean) -> Unit
) {
    val gridItems = (notes.map { GridItem.NoteItem(it) } + tasks.map { GridItem.TaskItem(it) })

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(gridItems) { gridItem ->
            when (gridItem) {
                is GridItem.NoteItem -> {
                    NoteCard(
                        note = gridItem.note,
                        onClick = { onNoteClick(gridItem.note, false) },
                        onLongClick = { onNoteClick(gridItem.note, true) }
                    )
                }
                is GridItem.TaskItem -> {
                    TaskCard(
                        task = gridItem.task,
                        onClick = { onTaskClick(gridItem.task, false) },
                        onLongClick = { onTaskClick(gridItem.task, true) }
                    )
                }
            }
        }
    }
}

sealed class GridItem {
    data class NoteItem(val note: Note) : GridItem()
    data class TaskItem(val task: Task) : GridItem()
}

@Preview(showBackground = true)
@Composable
fun NotesGridPreview() {
    NotesGrid(
        notes = listOf(
            Note(
                id = "1",
                title = "Title",
                imageUris = listOf("https://picsum.photos/200/300"),
                content = "Content",
                isFavorite = false,
                date = System.currentTimeMillis()
            ),
            Note(
                id = "2",
                title = "Another Title",
                imageUris = listOf(),
                content = "Some more content",
                isFavorite = true,
                date = System.currentTimeMillis()
            ),
            Note(
                id = "3",
                title = "Lorem Ipsum",
                imageUris = listOf(""),
                content = "There are many variations of passages of Lorem Ipsum available, " +
                        "but the majority have suffered alteration in some form, by injected humour," +
                        " or randomised words which don't look even slightly believable.",
                isFavorite = true,
                date = System.currentTimeMillis()
            ),
            Note(
                id = "4",
                title = "Lorem Ipsum",
                imageUris = listOf(""),
                content = "the majority have suffered alteration in some form, by injected humour," +
                        " or randomised words which don't look even slightly believable.",
                isFavorite = true,
                date = System.currentTimeMillis()
            )
        ), tasks = listOf(),
        onNoteClick = { _, _ -> }
    ) { _, _ -> }
}