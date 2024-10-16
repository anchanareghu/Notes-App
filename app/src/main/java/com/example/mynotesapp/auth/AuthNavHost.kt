package com.example.mynotesapp.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.MainActivity

@Composable
fun AuthNavHost(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            navigateToMainActivity(context)
        }
    }

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController)
        }
        composable("signup_screen") {
            SignUpScreen(navController) {
                authViewModel.login()
            }
        }
        composable("login_screen") {
            LoginScreen(navController) {
                authViewModel.login()
            }
        }
    }
}