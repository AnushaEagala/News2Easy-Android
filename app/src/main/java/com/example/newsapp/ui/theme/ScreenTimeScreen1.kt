package com.example.newsapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.newsapp.models.ScreenTimeData
import com.example.newsapp.viewmodels.ScreenTimeViewModel
import java.nio.file.WatchEvent
import androidx.compose.ui.Modifier

@Composable
fun ScreenTimeScreen1(viewModel: ScreenTimeViewModel = viewModel()) {
    val screenTimeData = viewModel.screenTimeData.collectAsState().value

    // Ensure the viewModel fetches data when the screen is launched
    viewModel.fetchMonthlyScreenTime()

    Column {
        Text(
            text = "Monthly Screen Time",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)  // Corrected padding reference
        )

        // Display the data using your graph component or other UI elements
        if (screenTimeData.isNotEmpty()) {
            ScreenTimeGraph1(screenTimeData) // Call your existing graph composable
        } else {
            Text("No screen time data available")
        }
    }
}
