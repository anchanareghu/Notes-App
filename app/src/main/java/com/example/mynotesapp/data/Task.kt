package com.example.mynotesapp.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Task(
    @DocumentId var id: String = "",
    val title: String = "",
    val taskEntries: List<TaskEntry> = listOf(),
    @PropertyName("favorite") val isFavorite: Boolean = false,
    @PropertyName("completed") var isCompleted: Boolean = false,
    val date: Long = System.currentTimeMillis(),
    val imageUris: List<String> = listOf()
)

data class TaskEntry(
    var text: String = "",
    var isChecked: Boolean = false
)
