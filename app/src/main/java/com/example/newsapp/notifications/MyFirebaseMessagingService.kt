package com.example.newsapp.notifications

import android.content.Context
import androidx.room.Room
import com.example.newsapp.MyApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationDao by lazy { MyApplication.database.notificationDao() }  // Ensure this happens after DB initialization

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            val notification = NotificationEntity(
                title = it.title.orEmpty(),
                body = it.body.orEmpty(),
                timestamp = System.currentTimeMillis()
            )
            storeNotificationLocally(notification)
        }
    }

    private fun storeNotificationLocally(notification: NotificationEntity) {
        GlobalScope.launch {
            try {
                notificationDao.insertNotification(notification)
            } catch (e: Exception) {
                // Handle database exceptions gracefully
            }
        }
    }
}
