package com.example.newsapp

import kotlinx.serialization.Serializable

@Serializable
object HomePageScreen

@Serializable
data class NewsArticleScreen(
    val title: String,
    val content: String,
    val urlToImage: String,
    val url: String,
    val author: String,
    val publishedAt: String
)
