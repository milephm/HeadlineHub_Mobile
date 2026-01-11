package com.example.news.ui.newslist.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.news.databinding.ItemNewsArticleBinding
import com.example.news.model.Article
import com.example.news.utils.formatNewsDate
import com.example.news.R
import com.google.android.material.R as MaterialR // Alias this one
import androidx.core.net.toUri

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var articles = listOf<Article>()
    private var onSaveClickListener: ((Article) -> Unit)? = null

    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    fun setOnSaveClickListener(listener: (Article) -> Unit) {
        onSaveClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position], onSaveClickListener)
    }

    override fun getItemCount() = articles.size

    class NewsViewHolder(private val binding: ItemNewsArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, onSaveClickListener: ((Article) -> Unit)?) {
            binding.titleTextView.text = article.title
            binding.descriptionTextView.text = article.description
            binding.sourceTextView.text = article.source?.name
            binding.sourceDateView.text = formatNewsDate(article.publishedAt)

            binding.openBrowser.setOnClickListener {
                openInBrowser(article.url)
            }

            article.urlToImage?.let { imageUrl ->
                binding.newsImageView.load(imageUrl) {
                    crossfade(true)
                    placeholder(MaterialR.drawable.ic_clock_black_24dp)
                    error(MaterialR.drawable.ic_mtrl_chip_close_circle)
                }
            }

            // 1. Set the initial icon based on whether it's already saved
            val initialIcon = if (article.isSaved) R.drawable.ic_favorite_filled_24 else R.drawable.ic_favorite_border_24
            binding.saved.setIconResource(initialIcon)

            // 2. Handle the click
            binding.saved.setOnClickListener {
                // Toggle the state locally for instant UI feedback
                article.isSaved = !article.isSaved

                // Update the icon immediately
                val newIcon =
                    if (article.isSaved) R.drawable.ic_favorite_filled_24 else R.drawable.ic_favorite_border_24
                binding.saved.setIconResource(newIcon)

                // Trigger the listener to save/delete in the database
                onSaveClickListener?.invoke(article)
            }
        }

        fun openInBrowser(url: String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
            binding.root.context.startActivity(browserIntent)
        }
    }
}