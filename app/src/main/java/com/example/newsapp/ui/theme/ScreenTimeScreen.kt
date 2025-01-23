package com.example.newsapp.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.newsapp.models.ScreenTimeData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
@Composable
fun ScreenTimeScreen() {
    val screenTimeData = remember { mutableStateListOf<ScreenTimeData>() }

    // Fetch the data when the screen is launched
    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()

            // Fetch screen time data from Firestore
            val result = db.collection("screenTime").get().await()

            val data = result.map { document ->
                val document = document  // Properly initialize `document` to avoid ambiguity
                ScreenTimeData(
                    date = document.getString("date") ?: "",
                    minutes = document.getLong("minutes")?.toInt() ?: 0,
                    timestamp = document.getLong("timestamp") ?: 0L,
                    screenTime = (document.getLong("screenTime") ?: 0L).toInt()
                )
            }

            screenTimeData.clear()  // Clear previous data
            screenTimeData.addAll(data)  // Add fetched data

            Log.d("Firestore", "Fetched screen time data: $data")
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching data: $e")
        }
    }

    // Show loading text if data is empty, otherwise show the chart
    if (screenTimeData.isEmpty()) {
        Text("Loading data...", modifier = Modifier.fillMaxSize())
    } else {
        ScreenTimeGraph(screenTimeData)  // Render the chart with the fetched data
    }
}
