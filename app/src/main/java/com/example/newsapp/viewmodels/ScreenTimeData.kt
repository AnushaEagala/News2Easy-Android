package com.example.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.newsapp.models.ScreenTimeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScreenTimeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _screenTimeData = MutableStateFlow<List<ScreenTimeData>>(emptyList())
    val screenTimeData: StateFlow<List<ScreenTimeData>> = _screenTimeData

    fun fetchMonthlyScreenTime() {
        val userId = auth.currentUser?.uid ?: return
        val startOfMonth = getStartOfMonth()

        db.collection("users")
            .document(userId)
            .collection("screenTime")
            .whereGreaterThanOrEqualTo("timestamp", startOfMonth)
            .get()
            .addOnSuccessListener { documents ->
                val data = documents.map { document ->
                    val timestamp = document.getLong("timestamp") ?: 0L
                    val screenTime = (document.getLong("screenTime") ?: 0L).toInt()
                    val date = formatDate(timestamp)

                    ScreenTimeData(
                        timestamp = timestamp,
                        screenTime = screenTime,
                        date = date,
                        minutes = screenTime / 60  // Convert seconds to minutes
                    )
                }
                _screenTimeData.value = data
            }
            .addOnFailureListener {
                // Handle error
            }
    }

    private fun getStartOfMonth(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(timestamp)
    }
}
