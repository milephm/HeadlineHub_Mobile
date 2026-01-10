package com.example.news.ui.newslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.api.NewsApiService
import com.example.news.ui.newslist.adapter.NewsAdapter
import com.example.news.databinding.NewsListFragmentBinding
import com.example.news.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.news_list_fragment) {
    private lateinit var _binding: NewsListFragmentBinding
    private val binding get() = _binding

    // Hilt will inject this automatically
    @Inject
    lateinit var newsApiService: NewsApiService
    private val newsAdapter = NewsAdapter()

    companion object {
        private const val BASE_URL = "https://newsapi.org/"
        private const val API_KEY = "2df34f56b1d74800ab06d57295dbedc2" // API key
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NewsListFragmentBinding.bind(view)

        setupRecyclerView()
        setupSwipeRefresh()
        setupNewsApi()
        fetchNews()
    }

    private fun setupRecyclerView() {
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchNews()
        }
    }

    private fun setupNewsApi() {
        newsApiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)

    }
    private fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsApiService.getTopHeadlines(apiKey = API_KEY)
                withContext(Dispatchers.Main) {
                    newsAdapter.updateArticles(response.articles)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error fetching news: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}