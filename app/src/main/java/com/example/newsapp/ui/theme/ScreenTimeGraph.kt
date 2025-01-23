package com.example.newsapp.ui.theme

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.example.newsapp.models.ScreenTimeData

@Composable
fun ScreenTimeGraph(screenTimeData: List<ScreenTimeData>) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            BarChart(context).apply {
                description.text = "Screen Time Over Time"
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                axisLeft.granularity = 1f // Ensure Y-axis labels are integers
                xAxis.granularity = 1f    // Ensure X-axis labels align with bars

                data = BarData(
                    BarDataSet(
                        screenTimeData.mapIndexed { index, data ->
                            BarEntry(index.toFloat(), data.minutes.toFloat()) // Use `minutes` property
                        },
                        "Screen Time (Minutes)"
                    ).apply {
                        color = Color.GREEN
                        valueTextColor = Color.BLACK
                        valueTextSize = 12f
                    }
                )
                invalidate() // Refresh the chart after setting data
            }
        }
    )
}
