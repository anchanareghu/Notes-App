package com.example.mynotesapp

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynotesapp.screens.getRandomColor

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    var color: Int = getRandomColor().toArgb(),
    val isFavorite: Boolean = false
)



