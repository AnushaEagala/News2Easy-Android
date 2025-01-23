package com.example.newsapp


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.intPreferencesKey


// Extension property to initialize DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

class DataStoreHelper(context: Context) {
    private val dataStore = context.dataStore

    // Function to get the share count for a specific article
    suspend fun getShareCount(articleId: String): Int {
        val prefs = dataStore.data.first()  // Get data from DataStore
        return prefs[intPreferencesKey("share_$articleId")] ?: 0
    }

    // Function to increment the share count for a specific article
    suspend fun incrementShareCount(articleId: String) {
        dataStore.edit { prefs ->
            val currentCount = prefs[intPreferencesKey("share_$articleId")] ?: 0
            prefs[intPreferencesKey("share_$articleId")] = currentCount + 1
        }
    }

    // Function to get the like count for a specific article
    suspend fun getLikeCount(articleId: String): Int {
        val prefs = dataStore.data.first()
        return prefs[intPreferencesKey("like_$articleId")] ?: 0
    }

    // Function to increment the like count for a specific article
    suspend fun incrementLikeCount(articleId: String) {
        dataStore.edit { prefs ->
            val currentCount = prefs[intPreferencesKey("like_$articleId")] ?: 0
            prefs[intPreferencesKey("like_$articleId")] = currentCount + 1
        }
    }

    // Function to decrement the like count for a specific article
    suspend fun decrementLikeCount(articleId: String) {
        dataStore.edit { prefs ->
            val currentCount = prefs[intPreferencesKey("like_$articleId")] ?: 0
            prefs[intPreferencesKey("like_$articleId")] = if (currentCount > 0) currentCount - 1 else 0
        }
    }
}