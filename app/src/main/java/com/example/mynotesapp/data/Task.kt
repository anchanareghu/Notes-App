package com.example.mynotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val taskEntries: List<TaskEntry> = listOf(),
    val isFavorite: Boolean = false,
    var isChecked: Boolean = false,
    val date: Long = System.currentTimeMillis(),
    val imageUris: List<String> = listOf()
)

data class TaskEntry(
    var text: String = "",
    var isChecked: Boolean = false
)
