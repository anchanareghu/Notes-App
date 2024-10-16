package com.example.mynotesapp.auth

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mynotesapp.MainActivity
import com.example.mynotesapp.R
import com.example.mynotesapp.ui.theme.MyNotesAppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SplashScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Get Started!",
            fontSize = 36.sp, color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(28.dp)
        )
        LoginScreenTextButton(
            text = "Login",
            onClick = { navController.navigate("login_screen") },
            background = Color.White,
            textColor = Color.Black
        )
        LoginScreenTextButton(
            text = "Sign Up",
            onClick = { navController.navigate("signup_screen") },
            background = Color.White,
            textColor = Color.Black
        )
    }
}

@Composable
fun LoginScreen(navController: NavController, signIn: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authenticationManager = AuthenticationManager(context)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign In",
            fontSize = 36.sp, color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(28.dp)
        )
        Text(text = "Please fill in the fields below to sign-in")
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = "",
                    tint = Color.Black
                )
            },
            label = { Text(text = "Enter your email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 12.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "",
                    tint = Color.Black
                )
            },
            label = { Text(text = "Enter your password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 12.dp)
        )
        ElevatedButton(
            onClick = {
                authenticationManager.login(email, password)
                    .onEach { response ->
                        when (response) {
                            is Response.Success -> {
                                signIn()
                                navigateToMainActivity(context)
                            }

                            is Response.Error -> {
                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.launchIn(coroutineScope)
            },
            colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 42.dp, vertical = 28.dp)
                .fillMaxWidth()
                .size(50.dp)
        ) {
            Text(text = "Sign In")
        }

        Row {
            Text(text = "New User? ")

            Text(
                text = "Sign Up",
                color = Color(0xff64B5F6),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("signup_screen")
                }
            )
        }


        ElevatedButton(
            onClick = {
                authenticationManager.signInWithGoogle()
                    .onEach { response ->
                        when (response) {
                            is Response.Success -> {
                                signIn()
                                navController.navigate("main_screen")
                            }

                            is Response.Error -> {
                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }.launchIn(coroutineScope)
            }, modifier = Modifier
                .padding(horizontal = 42.dp, vertical = 28.dp)
                .fillMaxWidth()
                .size(50.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = null, modifier = Modifier
                            .align(Alignment.Start)
                    )
                }
                Text(
                    text = "Continue with Google",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun LoginScreenTextButton(
    text: String,
    onClick: () -> Unit,
    background: Color,
    textColor: Color
) {
    TextButton(
        onClick = onClick,
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 12.dp)
            .background(background)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier
        )
    }
}

fun navigateToMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyNotesAppTheme {
        SplashScreen(navController = NavController(LocalContext.current))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    MyNotesAppTheme {
        LoginScreen(signIn = {}, navController = NavController(LocalContext.current))
    }
}