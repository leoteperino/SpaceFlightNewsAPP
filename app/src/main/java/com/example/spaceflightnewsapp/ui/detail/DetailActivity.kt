package com.example.spaceflightnewsapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = intent.getSerializableExtra("article") as? Article
        article?.let {
            binding.imageView.load(it.image_url)
            binding.tvTitle.text = it.title ?: ""
            binding.tvSummary.text = it.summary ?: ""
            binding.tvNewsSite.text = it.news_site ?: ""
            binding.tvPublishedAt.text = it.published_at ?: ""
            binding.tvUrl.text = it.url ?: ""
        }
    }
}