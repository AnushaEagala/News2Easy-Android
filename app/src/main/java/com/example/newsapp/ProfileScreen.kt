package com.example.newsapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    var recentActivity by remember { mutableStateOf("") }
    var savedArticles by remember { mutableStateOf(listOf<String>()) }
    var joinDate by remember { mutableStateOf("") }
    var preferredCategories by remember { mutableStateOf(listOf<String>()) }

    user?.let {
        // Fetching user-specific data from Firestore
        LaunchedEffect(user.uid) {
            db.collection("user_profiles").document(user.uid).get().addOnSuccessListener { document ->
                document?.let {
                    recentActivity = it.getString("recentActivity") ?: "No recent activity"
                    savedArticles = it.get("savedArticles") as? List<String> ?: listOf()
                    joinDate = it.getString("joinDate") ?: "Unknown"
                    preferredCategories = it.get("preferredCategories") as? List<String> ?: listOf()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
        }

        // Display the user's profile information
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile Page",
                style = MaterialTheme.typography.titleLarge // Use the correct typography
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Display Profile Picture Placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Profile Picture", modifier = Modifier.align(Alignment.Center))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Name: ${it.displayName ?: "Anonymous"}")
            Text("Email: ${it.email ?: "No email provided"}")
            Text("Join Date: $joinDate")

            Spacer(modifier = Modifier.height(16.dp))
            Text("Recent Activity: $recentActivity")

            Spacer(modifier = Modifier.height(16.dp))
            Text("Preferred Categories:")
            preferredCategories.forEach { category ->
                Text("- $category")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Saved Articles:")
            savedArticles.forEach { article ->
                Text("- $article")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { /* Navigate to Edit Profile */ }) {
                    Text("Edit Profile")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { signOut(context) }) {
                    Text("Sign Out")
                }
            }
        }
    } ?: run {
        // If no user is signed in
        Text("No user is signed in.")
    }
}

fun signOut(context: Context) {
    FirebaseAuth.getInstance().signOut()
    Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
}
