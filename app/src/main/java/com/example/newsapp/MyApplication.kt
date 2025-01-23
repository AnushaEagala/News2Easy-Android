package com.example.newsapp

import android.app.Application
import androidx.room.Room
import com.example.newsapp.notifications.NotificationDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: NotificationDatabase

        // Define the DATABASE_NAME constant
        private const val DATABASE_NAME = "notification_db"
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            NotificationDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}
