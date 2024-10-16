package com.example.mynotesapp.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthenticationManager
) : ViewModel() {

    var isLoggedIn by mutableStateOf(false)
        private set
    init {
        isLoggedIn = authManager.isUserLoggedIn()
    }

    fun login() {
        isLoggedIn = true
    }

    fun logout() {
        isLoggedIn = false
        authManager.logout()
    }
}