package com.example.newsapp.models

data class ScreenTimeData(
    val timestamp: Long,    // Unix timestamp
    val screenTime: Int,     // Screen time in seconds
    val date: String,        // Formatted date string (optional)
    val minutes: Int          // Screen time in minutes
)
