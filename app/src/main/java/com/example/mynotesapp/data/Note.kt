package com.example.mynotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val imageUri: String,
    val content: String,
    val isFavorite: Boolean,
    val isItalic: Boolean,
    val isBold: Boolean,
    val isUnderlined: Boolean,
    val date: Long,
)




