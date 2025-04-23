package com.example.mynotesapp.components.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.mynotesapp.data.Task
import com.example.mynotesapp.data.TaskEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val creationDate = task.date
    val formattedDate =
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(creationDate))

    Card(
        modifier = Modifier
            .heightIn(min = 100.dp, max = 300.dp)
            .padding(4.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),

        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.2f) else Color.DarkGray.copy(
                alpha = 0.2f
            )
        )
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
                if (task.isFavorite) {
                    Image(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "favorite",
                        modifier = Modifier
                            .size(18.dp),
                        colorFilter = ColorFilter.tint(Color.Red)
                    )
                }
            }
            if (task.imageUris.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(task.imageUris[0])
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
            if (task.title.isNotEmpty()) {
                Text(
                    text = task.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            if (task.taskEntries.isNotEmpty()) {
                LazyColumn {
                    items(task.taskEntries.size) {
                        TaskCheckBoxItem(
                            text = task.taskEntries[it].text,
                            checked = task.taskEntries[it].isChecked,
                            onChecked = {}
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(
        task = Task(
            id = 1,
            title = "task title",
            isFavorite = true,
            imageUris = listOf("https://picsum.photos/200/300"),
            date = System.currentTimeMillis(),
            isChecked = true,
            taskEntries = listOf(
                TaskEntry(
                    text = "task entry 1",
                    isChecked = true
                ),
                TaskEntry(
                    text = "task entry 2",
                    isChecked = false
                )
            )
        ),
        onClick = {},
        onLongClick = {}
    )
}