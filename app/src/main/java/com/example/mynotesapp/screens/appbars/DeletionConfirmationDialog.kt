package com.example.mynotesapp.screens.appbars

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeletionConfirmationDialog(onDeleteConfirmed: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Note") },
        text = { Text(text = "Are you sure you want to delete this note?") },
        confirmButton = {
            TextButton(onClick = onDeleteConfirmed) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
