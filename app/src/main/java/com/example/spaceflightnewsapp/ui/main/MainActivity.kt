package com.example.spaceflightnewsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.ActivityMainBinding
import com.example.spaceflightnewsapp.ui.detail.DetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private val loadingAdapter = LoadingAdapter()

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

        binding.searchEditText.clearFocus()

        binding.searchEditText.isEnabled = false
        binding.searchEditText.isEnabled = true

        setupRecyclerView()
        observeViewModel()
        setupSearch()
        setupSwipeToRefresh()

        viewModel.fetchArticles(limit = pageSize, offset = currentOffset)
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter { article ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }

        concatAdapter = ConcatAdapter(articleAdapter)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = concatAdapter

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

                val currentQuery = binding.searchEditText.text.toString()

                // ✅ Scroll solo si el usuario escribió una búsqueda
                if (currentQuery.isNotBlank() && currentOffset == 0) {
                    binding.recyclerView.post {
                        binding.recyclerView.scrollToPosition(0)
                    }
                }

                binding.emptyView.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.visibility = if (currentOffset == 0 && isLoading) View.VISIBLE else View.GONE

                if (currentOffset > 0) {
                    if (isLoading) {
                        if (!concatAdapter.adapters.contains(loadingAdapter)) {
                            concatAdapter.addAdapter(loadingAdapter)
                        }
                    } else {
                        if (concatAdapter.adapters.contains(loadingAdapter)) {
                            concatAdapter.removeAdapter(loadingAdapter)
                        }
                        isLoadingMore = false
                    }
                } else {
                    isLoadingMore = false
                }
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