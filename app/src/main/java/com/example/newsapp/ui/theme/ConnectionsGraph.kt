package com.example.newsapp.ui.theme

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.models.ConnectionData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis

@Composable
fun ConnectionsGraph(connectionData: List<ConnectionData>) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LineChart(context).apply {
                description.text = "Connections Over Time"

                xAxis.position = XAxis.XAxisPosition.BOTTOM

                axisRight.isEnabled = false

                val entries = connectionData.mapIndexed { index, data ->
                    Entry(index.toFloat(), data.connections.toFloat())
                }

                data = LineData(
                    LineDataSet(entries, "Connections").apply {
                        color = Color.BLUE
                        valueTextColor = Color.BLACK
                    }
                )
            }
        }
    )
}
