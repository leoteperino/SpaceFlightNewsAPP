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

    fun fetchArticles(limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getArticles(limit, offset)
                if (response.isSuccessful) {
                    val newArticles = response.body()?.results.orEmpty()
                    val currentList = _articles.value.toMutableList()
                    currentList.addAll(newArticles)
                    _articles.value = currentList
                }
            } catch (e: Exception) {
                // Log o manejo de error (futuro ErrorManager)
            } finally {
                _isLoading.value = false
            }
        }
    }
}