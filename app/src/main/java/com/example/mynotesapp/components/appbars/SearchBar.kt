package com.example.mynotesapp.components.appbars

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    val secondaryColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    TextField(
        value = searchQuery,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(56.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = secondaryColor
            )
        },
        trailingIcon = {},
        singleLine = true,
        placeholder = { Text(text = "Search notes..") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        keyboardActions = KeyboardActions(onSearch = {
            onValueChange(searchQuery)
        }),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = color,
            focusedContainerColor = color,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = secondaryColor,
            unfocusedTextColor = secondaryColor,
            focusedTextColor = secondaryColor,
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray
        )
    )
}
