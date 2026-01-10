package com.example.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null, // Room will auto-generate this
    val source: Source?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    var isSaved: Boolean = false
) : Serializable

data class Source(
    val id: String?,
    val name: String
) 