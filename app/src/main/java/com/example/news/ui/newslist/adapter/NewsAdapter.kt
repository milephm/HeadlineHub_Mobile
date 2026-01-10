package com.example.news.ui.newslist.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.news.databinding.ItemNewsArticleBinding
import com.example.news.model.Article
import com.example.news.utils.formatNewsDate
import com.google.android.material.R
import androidx.core.net.toUri

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var articles = listOf<Article>()

    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
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
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    class NewsViewHolder(private val binding: ItemNewsArticleBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(article: Article) {
            binding.titleTextView.text = article.title
            binding.descriptionTextView.text = article.description
            binding.sourceTextView.text = article.source.name
            binding.sourceDateView.text = formatNewsDate(article.publishedAt)

            binding.openBrowser.setOnClickListener {
                openInBrowser(article.url)
            }

            article.urlToImage?.let { imageUrl ->
                binding.newsImageView.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_clock_black_24dp)
                    error(R.drawable.ic_mtrl_chip_close_circle)
                }
            }
        }

        fun openInBrowser(url: String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
            binding.root.context.startActivity(browserIntent)
        }
    }
} 