package com.example.mynotesapp.components.tasks

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TaskCheckBoxItem(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = { onSelected() },
            colors = CheckboxDefaults.colors(
                checkedColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                uncheckedColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
            )
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultRadioButtonPreview() {
    TaskCheckBoxItem(
        text = "sample",
        selected = true,
        onSelected = {}
    )
}