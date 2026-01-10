package com.example.news.ui.newslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.api.NewsApiService
import com.example.news.ui.newslist.adapter.NewsAdapter
import com.example.news.ui.viewmodel.NewsViewModel
import com.example.news.databinding.NewsListFragmentBinding
import com.example.news.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.news_list_fragment) {
    private lateinit var _binding: NewsListFragmentBinding
    private val binding get() = _binding

    // Hilt will inject this automatically
    @Inject
    lateinit var newsApiService: NewsApiService
    private val newsAdapter = NewsAdapter()
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NewsListFragmentBinding.bind(view)

        setupRecyclerView()
        setupObservers()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun setupObservers() {
        // 1. Observe the news list
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            newsAdapter.updateArticles(articles)
        }

        // 2. Observe loading state (for the spinner)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        // 3. Observe errors
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchNews()
        }
    }
}