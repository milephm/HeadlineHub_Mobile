package com.example.news.di

import android.content.Context
import com.example.news.data.database.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import androidx.room.Room
import com.example.news.data.database.ArticleDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(
        @ApplicationContext app: Context
    ): ArticleDatabase {
        return Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            "news_db.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: ArticleDatabase): ArticleDao {
        return db.getArticleDao()
    }
}