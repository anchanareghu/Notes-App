package com.example.mynotesapp.components.appbars

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DeletionConfirmationDialog(onDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Note", fontWeight = FontWeight.Bold) },
        text = { Text(text = "Are you sure you want to delete this note?") },
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
        textContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
        titleContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
        iconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(
                    text = "Delete",
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }
    )
}
