package com.example.news.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.model.Article

@Dao
interface ArticleDao {

    // OnConflictStrategy.REPLACE means if we save the same article twice, update it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles WHERE url = :url")
    suspend fun deleteArticleByUrl(url: String)

    @Query("SELECT url FROM articles")
    suspend fun getAllSavedUrls(): List<String>

    // Check if an article exists (useful for toggling the heart icon)
    @Query("SELECT EXISTS(SELECT * FROM articles WHERE url = :url)")
    suspend fun isArticleSaved(url: String): Boolean
}