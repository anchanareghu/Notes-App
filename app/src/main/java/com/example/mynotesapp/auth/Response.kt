package com.example.mynotesapp.auth

interface Response {
    data class Success(val username: String? = null) : Response
    data class Error(val message: String) : Response
}