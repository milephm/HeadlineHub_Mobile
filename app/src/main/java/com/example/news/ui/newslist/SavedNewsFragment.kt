package com.example.news.ui.newslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.ui.newslist.adapter.NewsAdapter
import com.example.news.databinding.SavedListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment(R.layout.saved_list_fragment) {

    private lateinit var _binding: SavedListFragmentBinding
    private val binding get() = _binding
    private val newsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SavedListFragmentBinding.bind(view)

        setupRecyclerView()
        // Here is where you will eventually observe data from your Room Database
    }

    private fun setupRecyclerView() {
        binding.savedNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }
}