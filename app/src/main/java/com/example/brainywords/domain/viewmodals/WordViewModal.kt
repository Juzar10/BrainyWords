package com.example.brainywords.domain.viewmodals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainywords.data.model.Word
import com.example.brainywords.data.repository.WordRepository
import kotlinx.coroutines.launch

class WordViewModel(
    private val repository: WordRepository
) : ViewModel() {

    var words by mutableStateOf<List<Word>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var currentBatchIndex = 0
    private val batchSize = 10
    private var previousPage = -1

    init {
        loadInitialWords()
    }

    private fun loadInitialWords() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.fetchWords(currentBatchIndex, batchSize)
                words = repository.words
                currentBatchIndex += batchSize
            } catch (e: Exception) {
                return@launch
            } finally {
                isLoading = false
            }
        }
    }

    fun onPageChanged(newPage: Int) {
        // Update view count for the previous page when user swipes away
        if (previousPage != -1 && previousPage != newPage) {
            val previousWord = getWordForPage(previousPage)
            previousWord?.let { word ->
                incrementViewCount(word.id)
            }
        }

        previousPage = newPage

        // Load more words if needed
        val wordsNeeded = newPage + 5 // Load ahead buffer
        if (wordsNeeded >= words.size && !isLoading) {
            loadMoreWords()
        }
    }

    private fun loadMoreWords() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.fetchWords(currentBatchIndex, batchSize)
                words = repository.words
                currentBatchIndex += batchSize
            } catch (e: Exception) {
                return@launch;
            } finally {
                isLoading = false
            }
        }
    }

    private fun incrementViewCount(wordId: Int) {
        viewModelScope.launch {
            try {
                val success = repository.incrementViewCount(wordId)
                if (success) {
                    // Update the local state
                    words = repository.words
                }
            } catch (e: Exception) {
                return@launch;
            }
        }
    }

    fun getWordForPage(page: Int): Word? {
        return if (page < words.size) words[page] else null
    }

    fun shouldShowLoading(page: Int): Boolean {
        return page >= words.size && isLoading
    }
}