package com.example.newsapp






import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope

import java.time.ZoneOffset
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Composable
fun NewsArticlePage(
    title: String,
    content: String,
    urlToImage: String?,
    url: String,
    author: String,
    publishedAt: String,
    context: Context,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val dataStoreHelper = remember { DataStoreHelper(context) }
    var shareCount by remember { mutableStateOf(0) }
    var likeCount by remember { mutableStateOf(0) }
    var liked by remember { mutableStateOf(false) }

    // Get the current share count for this specific article
    LaunchedEffect(url) {
        shareCount = dataStoreHelper.getShareCount(url)
        likeCount = dataStoreHelper.getLikeCount(url)
    }
    val timeAgo = getTimeAgo(publishedAt)

    Column(modifier = modifier.padding(20.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 25.sp)
        Text(text = author, fontWeight = FontWeight.Bold)
        Text(text = "Published: $timeAgo", fontSize = 14.sp)

        AsyncImage(
            model = urlToImage ?: "https://st4.depositphotos.com/17828278/24401/v/450/depositphotos_244011872-stock-illustration-image-vector-symbol-missing-available.jpg",
            contentDescription = "Article Image",
            modifier = Modifier.fillMaxWidth().size(200.dp),
            contentScale = ContentScale.Crop
        )

        Text(text = content)
        Icon(imageVector = Icons.Default.Share,
            contentDescription = "Share",
            modifier = Modifier
                .size(24.dp)
                .clickable {

                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        dataStoreHelper.incrementShareCount(url)
                    }
                    // Trigger the share intent
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this article: $title\n\nRead more here: $url")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Article via"))
                }
        )
        Text(text = "Shared $shareCount times", fontSize = 14.sp)

        // Like Icon
        Icon(
            imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    liked = !liked
                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        if (liked) {
                            dataStoreHelper.incrementLikeCount(url)
                        } else {
                            dataStoreHelper.decrementLikeCount(url)
                        }
                        likeCount = dataStoreHelper.getLikeCount(url)
                    }
                },
            tint = if (liked) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Gray
        )
        Text(text = "Liked $likeCount times", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
    }

}

fun getTimeAgo(publishedAt: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val publishedDate = ZonedDateTime.parse(publishedAt, formatter)
        val now = ZonedDateTime.now(ZoneOffset.UTC)

        val minutesAgo = ChronoUnit.MINUTES.between(publishedDate, now)
        val hoursAgo = ChronoUnit.HOURS.between(publishedDate, now)
        val daysAgo = ChronoUnit.DAYS.between(publishedDate, now)

        when {
            minutesAgo < 1 -> "Just now"
            minutesAgo < 60 -> "${minutesAgo}m ago"
            hoursAgo < 24 -> "${hoursAgo}h ago"
            else -> "${daysAgo}d ago"
        }
    } catch (e: Exception) {
        "Unknown time"
    }
}
