package com.example.mynotesapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mynotesapp.components.home.MainScreen
import com.example.mynotesapp.components.notes.NewNoteScreen
import com.example.mynotesapp.components.notes.NotesEditorScreen
import com.example.mynotesapp.components.tasks.NewTaskScreen
import com.example.mynotesapp.components.tasks.TaskEditorScreen


@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {

        composable("main_screen") {
            MainScreen(navController)
        }
        composable("new_note") {
            NewNoteScreen(navController)
        }
        composable("edit_note/{noteId}") {
            NotesEditorScreen(navController)
        }
        composable("new_task") {
            NewTaskScreen(navController)
        }
        composable("edit_task/{taskId}") {
            TaskEditorScreen(navController)
        }
    }
}

