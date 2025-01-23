package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.NewsViewModel
import com.example.newsapp.navigation.AppNavigation
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.ScreenTimeTracker

class MainActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        ScreenTimeTracker.startTracking()
    }

    override fun onStop() {
        super.onStop()
        ScreenTimeTracker.stopTracking()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF81BDB4), Color(0xFFC98989))
                            )
                        )
                ) {
                    // Position the text at the top center
                    Text(
                        text = "NEWS2EASY",
                        modifier = Modifier
                            .align(Alignment.TopCenter) // Align text at the top center of the Box
                            .padding(top = 16.dp), // Add some padding from the top
                        fontSize = 25.sp,
                        fontFamily = FontFamily.Serif
                    )

                    // Create navigation controller and view models
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val newsViewModel: NewsViewModel = viewModel()

                    // Pass navigation controller and view models to AppNavigation
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        newsViewModel = newsViewModel
                    )
                }
            }
        }
    }
}
