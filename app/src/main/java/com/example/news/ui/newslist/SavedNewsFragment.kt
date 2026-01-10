package com.example.news.ui.newslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.ui.newslist.adapter.NewsAdapter
import com.example.news.databinding.SavedListFragmentBinding
import com.example.news.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment(R.layout.saved_list_fragment) {

    private lateinit var _binding: SavedListFragmentBinding
    private val binding get() = _binding

    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SavedListFragmentBinding.bind(view)

        setupRecyclerView()
        setupObservers()

        // Here is where you will eventually observe data from your Room Database
        // Handle clicks: If user clicks the heart here, they want to DELETE it
        newsAdapter.setOnSaveClickListener { article ->
            viewModel.deleteArticle(article)
            Toast.makeText(requireContext(), "Removed from Saved", Toast.LENGTH_SHORT).show()
            // Note: The observer below will automatically update the list,
            // so you don't need to manually remove it from the adapter.
    }
        }

    private fun setupRecyclerView() {
        binding.savedNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun setupObservers() {
        // Observe the database directly
        viewModel.getSavedNews().observe(viewLifecycleOwner) { articles ->
            newsAdapter.updateArticles(articles)

            // Optional: Show "No saved articles" text if list is empty
            if (articles.isEmpty()) {
                binding.emptyStateText.visibility = View.VISIBLE
            } else {
                binding.emptyStateText.visibility = View.GONE
            }
        }
    }
}