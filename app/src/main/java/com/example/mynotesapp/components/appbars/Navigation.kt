package com.example.mynotesapp.components.appbars

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.viewmodel.NotesViewModel
import com.example.mynotesapp.components.MainScreen
import com.example.mynotesapp.components.NotesEditorScreen

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



