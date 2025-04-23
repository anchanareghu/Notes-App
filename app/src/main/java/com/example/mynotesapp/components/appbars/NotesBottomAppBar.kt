package com.example.mynotesapp.components.appbars

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.mynotesapp.R

@Composable
fun NotesBottomAppBar(
    onImageClick: () -> Unit,
    onAddClick: () -> Unit,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderlineClick: () -> Unit,
) {
    BottomAppBar(
        actions = {
            Row {
                IconButton(
                    onClick = onImageClick,
                    colors = IconButtonColors(
                        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_image_24),
                        contentDescription = "Add Image",
                    )
                }
                IconButton(
                    onClick = onBoldClick,
                    colors = IconButtonColors(
                        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_bold_24),
                        contentDescription = "Bold Text",
                    )
                }
                IconButton(
                    onClick = onItalicClick,
                    colors = IconButtonColors(
                        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_italic_24),
                        contentDescription = "Italic Text",
                    )
                }
                IconButton(
                    onClick = onUnderlineClick,
                    colors = IconButtonColors(
                        contentColor = if(isSystemInDarkTheme()) Color.White else Color.Black,
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_underlined_24),
                        contentDescription = "Underline Text",
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Done, contentDescription = "Save")
            }
        }
    )
}