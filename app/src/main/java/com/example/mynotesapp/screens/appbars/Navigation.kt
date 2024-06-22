package com.example.mynotesapp.screens.appbars

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.NotesViewModel
import com.example.mynotesapp.screens.MainScreen
import com.example.mynotesapp.screens.NotesEditorScreen

@Composable
fun NotesAppNavHost(notesViewModel: NotesViewModel) {
    val navController = rememberNavController()
    NavHost( navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController, notesViewModel)
        }
        composable("new_note") {
            NotesEditorScreen(navController, notesViewModel)
        }
        composable("note_detail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt() ?: -1
            NotesEditorScreen(navController, notesViewModel, noteId)
        }
    }
}



