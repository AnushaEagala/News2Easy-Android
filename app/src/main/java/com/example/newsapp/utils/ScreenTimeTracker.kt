package com.example.newsapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object ScreenTimeTracker {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var sessionStartTime: Long = 0

    fun startTracking() {
        sessionStartTime = System.currentTimeMillis()
    }

    fun stopTracking() {
        val sessionEndTime = System.currentTimeMillis()
        val screenTime = sessionEndTime - sessionStartTime

        val userId = auth.currentUser?.uid ?: return
        val currentDate = System.currentTimeMillis()

        val data = hashMapOf(
            "timestamp" to currentDate,
            "screenTime" to screenTime
        )

        db.collection("users")
            .document(userId)
            .collection("screenTime")
            .add(data)
            .addOnSuccessListener {
                // Successfully logged screen time
            }
            .addOnFailureListener {
                // Handle error
            }
    }
}
