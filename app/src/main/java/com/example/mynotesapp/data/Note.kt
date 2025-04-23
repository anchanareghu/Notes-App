package com.example.mynotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val imageUris: List<String> = listOf(" "),
    val isFavorite: Boolean = false,
    val date: Long = System.currentTimeMillis()
)

