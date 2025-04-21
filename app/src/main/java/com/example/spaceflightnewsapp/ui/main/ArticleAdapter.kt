package com.example.spaceflightnewsapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.databinding.ItemArticleBinding

class ArticleAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvTitle.text = article.title ?: "Sin título"
            binding.tvSummary.text = article.summary ?: "Sin resumen"
            binding.imageProgressBar.visibility = View.VISIBLE

            binding.imageView.load(article.image_url) {
                crossfade(true)
                error(R.drawable.image_error)

                listener(
                    onSuccess = { _, _ ->
                        binding.imageProgressBar.visibility = View.GONE
                    },
                    onError = { _, _ ->
                        binding.imageProgressBar.visibility = View.GONE
                    }
                )
            }

            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem
    }
}