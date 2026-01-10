package com.example.news.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.NewsRepository
import com.example.news.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    // Helper class to store the list in memory
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    companion object {
        private const val API_KEY = "2df34f56b1d74800ab06d57295dbedc2" // API key
    }

    // This init block runs ONLY when the ViewModel is first created.
    // It won't run again when you switch tabs back and forth.
    init {
        fetchNews()
    }

    fun fetchNews() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Adjust this call to match your actual API method signature
                val response = repository.getTopHeadlines(apiKey = API_KEY)
                val fetchArticles = response.articles

                val savedUrls = repository.getSavedUrls().toSet()

                fetchArticles.forEach { article ->
                    // If the article URL is in our saved list, mark it as true
                    if (article.url in savedUrls) {
                        article.isSaved = true
                    }
                }

                _articles.value = fetchArticles
                _error.value = null

            } catch (e: Exception) {
                _error.value = "Error fetching news: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    // 2. Delete Article
    fun deleteArticle(article: Article) = viewModelScope.launch {
        if (article.id != null) {
            repository.delete(article)
        } else {
            article.url.let { url ->
                repository.deleteByUrl(url)
            }
        }
    }

    // 3. Get Saved News (Observe this in SavedNewsFragment)
    fun getSavedNews() = repository.getSavedNews()


    fun refreshState() {
        viewModelScope.launch {
            // 1. Get current list from memory (don't call API)
            val currentArticles = _articles.value ?: return@launch
            if (currentArticles.isEmpty()) return@launch

            // 2. Get latest saved URLs from DB
            val savedUrls = repository.getSavedUrls().toSet()

            // 3. Update the 'isSaved' flag for each article
            currentArticles.forEach { article ->
                article.isSaved = article.url in savedUrls
            }

            // 4. Force the LiveData to update so the Adapter refreshes
            _articles.postValue(currentArticles)
        }
    }
}