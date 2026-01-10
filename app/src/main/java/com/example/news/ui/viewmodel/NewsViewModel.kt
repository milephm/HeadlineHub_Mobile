package com.example.news.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.api.NewsApiService
import com.example.news.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsApiService: NewsApiService
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

//    fun fetchNews() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = newsApiService.getTopHeadlines(apiKey = API_KEY)
//                withContext(Dispatchers.Main) {
//                    newsAdapter.updateArticles(response.articles)
//                    binding.swipeRefreshLayout.isRefreshing = false
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        "Error fetching news: ${e.message}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    binding.swipeRefreshLayout.isRefreshing = false
//                }
//            }
//        }
//    }

    fun fetchNews() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Adjust this call to match your actual API method signature
                val response = newsApiService.getTopHeadlines(apiKey = API_KEY)
                _articles.value = response.articles
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error fetching news: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}