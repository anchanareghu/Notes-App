package com.example.mynotesapp.components.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotesapp.auth.AuthActivity
import com.example.mynotesapp.auth.AuthenticationManager
import com.example.mynotesapp.auth.navigateToMainActivity
import com.example.mynotesapp.firestore.FireStoreRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val authManager = AuthenticationManager(context)

    val user = authManager.auth.currentUser
    val firestoreRepo = remember { FireStoreRepo(FirebaseFirestore.getInstance()) }

    var userName by remember {
        mutableStateOf(
            user?.displayName ?: ""
        )
    }
    val email by remember { mutableStateOf(user?.email ?: "Guest@notesapp") }
    var photoUrl by remember {
        mutableStateOf(
            user?.photoUrl?.toString() ?: ""
        )
    }
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firestoreRepo.getProfileDetails(
            onSuccess = { fetchedUserName, fetchedPhotoUrl ->
                userName = fetchedUserName
                photoUrl = fetchedPhotoUrl
            },
            onFailure = {
                Toast.makeText(context, "Failed to fetch profile details", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }
    Column(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (user != null) {
            ProfileImage(
                imageUrl = Uri.parse(photoUrl),
                onImageChangeClick = { newUri ->
                    updateProfilePicture(user, newUri)
                    photoUrl = newUri.toString()
                },
                isEditing = isEditing
            )
        } else {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "Default Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isSystemInDarkTheme()) Color.White else Color.Black,
                        CircleShape
                    )
                    .background(Color.Transparent)
            )
        }

        // Editable Username Field
        if (isEditing) {
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        } else {
            Text(
                text = userName.ifEmpty { "Guest" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        }

        // Email Display
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Edit/Save Button
        Button(
            onClick = {
                if (isEditing) {
                    updateUserProfile(user, userName, Uri.parse(photoUrl))
                    firestoreRepo.saveProfileDetails(userName, photoUrl)

                }
                isEditing = !isEditing
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White
            )
        ) {
            Text(text = if (isEditing) "Save" else "Edit Profile")
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, AuthActivity::class.java))
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White
            ),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Login with another account",
                modifier = Modifier.padding(4.dp)
            )
        }

        // Logout Button
        TextButton(
            onClick = {
                authManager.logout()
                navigateToMainActivity(context)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Logout", color = Color.Red)
        }
    }
}


private fun updateProfilePicture(user: FirebaseUser, uri: Uri) {
    val profileUpdates = UserProfileChangeRequest.Builder()
        .setPhotoUri(uri)
        .build()

    user.updateProfile(profileUpdates).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("ProfileScreen", "User profile picture updated.")
        }
    }
}

private fun updateUserProfile(user: FirebaseUser?, newUserName: String, photoUri: Uri) {
    user?.let {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newUserName)
            .setPhotoUri(photoUri)
            .build()

        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ProfileScreen", "User profile updated.")
            } else {
                Log.d("ProfileScreen", "User profile update failed.")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}