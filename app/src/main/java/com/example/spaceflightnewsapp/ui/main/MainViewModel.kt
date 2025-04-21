package com.example.spaceflightnewsapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError

    private val repository = ArticleRepository()

    private val _allArticles = MutableStateFlow<List<Article>>(emptyList())
    private val _filteredArticles = MutableStateFlow<List<Article>>(emptyList())
    val filteredArticles: StateFlow<List<Article>> = _filteredArticles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            combine(_allArticles, searchQuery) { articles, query ->
                if (query.isBlank()) {
                    articles
                } else {
                    articles.filter {
                        it.title?.contains(query, ignoreCase = true) == true ||
                                it.summary?.contains(query, ignoreCase = true) == true
                    }
                }
            }.collectLatest { filtered ->
                _filteredArticles.value = filtered
            }
        }
    }

    fun fetchArticles(limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val data = repository.getArticles(limit, offset)
                val newArticles = data.results.orEmpty()
                val currentList = _allArticles.value.toMutableList()
                currentList.addAll(newArticles)
                _allArticles.value = currentList

                _showError.value = false
            } catch (e: Exception) {
                _showError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun clearArticles() {
        _allArticles.value = emptyList()
    }
}
