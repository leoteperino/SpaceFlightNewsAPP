package com.example.spaceflightnewsapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    private var isLoadingMore = false
    private var currentOffset = 0
    private val pageSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.space_flight_news)
        binding.toolbar.setTitleTextColor(getColor(android.R.color.black))

        setupRecyclerView()
        observeViewModel()
        setupSearch()
        setupSwipeToRefresh()

        viewModel.fetchArticles(limit = pageSize, offset = currentOffset)
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = articleAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (!isLoadingMore && lastVisibleItem >= totalItemCount - 3) {
                        isLoadingMore = true
                        currentOffset += pageSize
                        viewModel.fetchArticles(limit = pageSize, offset = currentOffset)
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.filteredArticles.collectLatest { articles ->
                articleAdapter.submitList(articles)
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition(0)
                }
                isLoadingMore = false
                binding.emptyView.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
        }
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.searchEditText.text.isNullOrEmpty()) {
                binding.searchEditText.setText("")
                binding.searchEditText.clearFocus()
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            currentOffset = 0
            viewModel.clearArticles()
            viewModel.fetchArticles(limit = pageSize, offset = currentOffset)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}