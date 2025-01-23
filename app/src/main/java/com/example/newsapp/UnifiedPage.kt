package com.example.newsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.newsapp.utils.shareViaGmail
import com.example.newsapp.utils.shareViaSMS
import com.example.newsapp.utils.shareViaSocialMedia
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.example.finalproject.NewsViewModel
import com.kwabenaberko.newsapilib.models.Article
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnifiedPage(
    newsViewModel: NewsViewModel,
    navController: NavHostController,
    onNavigateToProfile: () -> Unit,
    onNavigateToConnectionsGraph: () -> Unit,
    onNavigateToScreenTimeGraph: () -> Unit
) {
    val articles by newsViewModel.articles.observeAsState(emptyList())
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Section with Navigation and Sharing Options
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Image(
                        painter = painterResource(id = R.drawable.menu_icon),
                        contentDescription = "Menu Icon",
                        modifier = Modifier.size(32.dp)
                    )
                }



                IconButton(onClick = {
                    shareViaGmail(context, "Subject for Gmail", "Body content for Gmail")
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.share_icon),
                        contentDescription = "Share Icon",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(250.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("View Connections") },
                    onClick = {
                        navController.navigate("connectionsGraph") // Direct navigation
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("View Screen Time") },
                    onClick = {
                        navController.navigate("screenTimeGraph") // Direct navigation
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Perofile") },
                    onClick = {
                        onNavigateToProfile()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Share via Gmail") },
                    onClick = {
                        shareViaGmail(context, "Subject for Gmail", "Body content for Gmail")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Share via SMS") },
                    onClick = {
                        shareViaSMS(context, "Body content for SMS")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Share on Social Media") },
                    onClick = {
                        shareViaSocialMedia(
                            context,
                            "Subject for Social Media",
                            "Body content for Social Media"
                        )
                        expanded = false
                    }
                )
            }
        }

        // Main Content
        UnifiedPage(newsViewModel, navController)
    }
}

@Composable
fun UnifiedPage(newsViewModel: NewsViewModel, navController: NavHostController) {
    val articles by newsViewModel.articles.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        CategoriesBar(newsViewModel)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(articles) { article ->
                ArticleItem(article, navController)
            }
        }
    }
}

@Composable
fun CategoriesBar(newsViewModel: NewsViewModel) {
    val categoriesList = listOf(
        "General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categoriesList.forEach { category ->
            Button(
                onClick = { newsViewModel.fetchNewsTopHeadlines(category) },
                modifier = Modifier.padding(5.dp)
            ) {
                Text(text = category)
            }
        }
    }
}


@Composable
fun ArticleItem(article: Article, navController: NavHostController) {
    val timeAgo = getTimeAgo(article.publishedAt)
    val context = LocalContext.current
    val dataStoreHelper = remember { DataStoreHelper(context) }
    var shareCount by remember { mutableStateOf(0) }
    var likeCount by remember { mutableStateOf(0) }

    val articleId = article.url ?: ""

    // Get the current share count for this specific article
    LaunchedEffect(articleId) {
        shareCount = dataStoreHelper.getShareCount(articleId)
        likeCount = dataStoreHelper.getLikeCount(articleId)
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            val encodedTitle = URLEncoder.encode(article.title, StandardCharsets.UTF_8.toString())
            val encodedContent = URLEncoder.encode(article.content ?: "", StandardCharsets.UTF_8.toString())
            val encodedUrlToImage = URLEncoder.encode(article.urlToImage ?: "", StandardCharsets.UTF_8.toString())
            val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
            val encodedAuthor = URLEncoder.encode(article.author ?: "", StandardCharsets.UTF_8.toString())
            val encodedPublishedAt = URLEncoder.encode(article.publishedAt, StandardCharsets.UTF_8.toString())

            navController.navigate(
                "article/$encodedTitle/$encodedContent/$encodedUrlToImage/$encodedUrl/$encodedAuthor/$encodedPublishedAt"
            )
        }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage
                    ?: "https://st4.depositphotos.com/17828278/24401/v/450/depositphotos_244011872-stock-illustration-image-vector-symbol-missing-available.jpg",
                contentDescription = "Image of the article",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = article.source.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = article.title, maxLines = 2)
                Text(text = timeAgo, style = MaterialTheme.typography.bodySmall)


                Row {
                    Text(text = timeAgo, maxLines = 1)
                    Icon(imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 8.dp))
                    Text(text = "$shareCount" , fontSize = 14.sp,modifier = Modifier.padding(start = 8.dp))

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 16.dp))
                    Text(text = "$likeCount" , fontSize = 14.sp,modifier = Modifier.padding(start = 8.dp))



                }
            }
        }
    }
}