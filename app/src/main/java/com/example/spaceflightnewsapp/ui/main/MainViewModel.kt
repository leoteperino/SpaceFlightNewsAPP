package com.example.spaceflightnewsapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = ArticleRepository()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getArticles()
                if (response.isSuccessful) {
                    _articles.value = response.body()?.results.orEmpty()
                } else {
                    _articles.value = emptyList()
                }
            } catch (e: Exception) {
                _articles.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}