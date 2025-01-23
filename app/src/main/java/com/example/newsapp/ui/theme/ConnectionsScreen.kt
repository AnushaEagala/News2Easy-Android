package com.example.newsapp.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.newsapp.models.ConnectionData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ConnectionsScreen() {
    val connectionData = remember { mutableStateListOf<ConnectionData>() }
    val isLoading = remember { mutableStateOf(true) }  // Track loading state
    val errorMessage = remember { mutableStateOf<String?>(null) }  // Track error messages

    // Fetch the data when the screen is launched
    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()

            // Fetch connections data from Firestore
            val result = db.collection("connections").get().await()

            val data = result.map { document ->
                ConnectionData(
                    date = document.getString("date") ?: "",
                    connections = document.getLong("connections")?.toInt() ?: 0
                )
            }

            connectionData.clear()  // Clear previous data
            connectionData.addAll(data)  // Add fetched data
            isLoading.value = false  // Data fetched, stop loading

            Log.d("Firestore", "Fetched connections data: $data")
        } catch (e: Exception) {
            isLoading.value = false  // Stop loading in case of error
            errorMessage.value = "Error fetching data: ${e.localizedMessage}" // Set error message
            Log.e("Firestore", "Error fetching data: ${e.localizedMessage}", e)
        }
    }

    // Show loading text if data is empty, otherwise show the chart
    if (isLoading.value) {
        Text("Loading data...", modifier = Modifier.fillMaxSize())
    } else if (errorMessage.value != null) {
        Text("Error: ${errorMessage.value}", modifier = Modifier.fillMaxSize())
    } else {
        ConnectionsGraph(connectionData)  // Render the chart with the fetched data
    }
}
