package com.example.mynotesapp.components.appbars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.auth.AuthViewModel
import com.example.mynotesapp.components.home.MainScreen
import com.example.mynotesapp.components.notes.NewNoteScreen
import com.example.mynotesapp.components.notes.NotesEditorScreen
import com.example.mynotesapp.components.home.ProfileScreen
import com.example.mynotesapp.components.tasks.NewTaskScreen
import com.example.mynotesapp.components.tasks.TaskEditorScreen

@Composable
fun NotesAppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            navController.navigate("main_screen")
        }
    }

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
        composable("profile") {
            ProfileScreen()
        }
        composable("new_task"){
            NewTaskScreen(navController)
        }
        composable("edit_task/{taskId}"){
            TaskEditorScreen(navController)
        }
    }
}
