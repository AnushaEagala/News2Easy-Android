package com.example.newsapp.notifications

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.newsapp.MyApplication
import kotlinx.coroutines.launch

@Composable
fun NotificationsScreen() {
    val context = LocalContext.current
    val notificationDao = MyApplication.database.notificationDao()
    val notificationList = remember { mutableStateListOf<String>() }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Fetch notifications from Room database
        coroutineScope.launch {
            try {
                // Fetch notifications from Room database
                val notifications = notificationDao.getAllNotifications()
                notifications.forEach {
                    val notificationText = "${it.title} - ${it.body}"
                    notificationList.add(notificationText)
                }
            } catch (e: Exception) {
                // Handle any exceptions gracefully
            }
        }
    }

    LazyColumn {
        items(notificationList) { notification ->
            Text(text = notification)
        }
    }
}
