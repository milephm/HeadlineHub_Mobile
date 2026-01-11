package com.example.news.data.repository

import com.example.news.api.NewsApiService
import com.example.news.data.database.ArticleDatabase
import com.example.news.model.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val db: ArticleDatabase
) {

    // 1. Network Call
    suspend fun getTopHeadlines(apiKey: String) =
        newsApiService.getTopHeadlines(apiKey = apiKey)

    // 2. Database Calls
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article)

    suspend fun deleteByUrl(url: String) = db.getArticleDao().deleteArticleByUrl(url)

    suspend fun getSavedUrls(): List<String> {
        return db.getArticleDao().getAllSavedUrls()
    }

    fun getSavedNews() = db.getArticleDao().getAllArticles()
}