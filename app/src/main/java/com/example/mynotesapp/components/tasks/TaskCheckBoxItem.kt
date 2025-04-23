package com.example.mynotesapp.components.tasks

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TaskCheckBoxItem(
    text: String,
    checked: Boolean,
    onChecked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onChecked() },
            colors = CheckboxDefaults.colors(
                checkedColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                uncheckedColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
            )
        )

        Text(
            text = text,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            ),
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultRadioButtonPreview() {
    TaskCheckBoxItem(
        text = "sample",
        checked = true,
        onChecked = {}
    )
}