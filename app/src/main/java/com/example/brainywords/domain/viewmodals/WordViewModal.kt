package com.example.brainywords.domain.viewmodals

import com.example.brainywords.data.repository.WordRepository

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainywords.data.model.Word
import kotlinx.coroutines.launch

class WordViewModel(
    private val repository: WordRepository
) : ViewModel() {

    val words = mutableStateListOf<Word>()
    var windowStart = 0
        private set
    var isLoading = false
        private set
    var hasMoreWords = true
        private set

    private val windowSize = 15
    private val batchSize = 5
    private val threshold = 3

    init {
        loadInitialWords()
    }

    private fun loadInitialWords() {
        viewModelScope.launch {
            isLoading = true
            try {
                val initialWords = repository.getInitialWords(batchSize = windowSize)
                words.clear()
                words.addAll(initialWords)
                windowStart = 0
            } catch (e: Exception) {
                hasMoreWords = false
            } finally {
                isLoading = false
            }
        }
    }

    fun onPageChanged(currentPage: Int) {
        val relativeIndex = currentPage - windowStart
        if (relativeIndex >= words.size - threshold && !isLoading && hasMoreWords) {
            loadNextWords()
        } else if (relativeIndex <= threshold && windowStart > 0 && !isLoading) {
            loadPreviousWords()
        }
    }

    private fun loadNextWords() {
        viewModelScope.launch {
            isLoading = true
            try {
                val newWords = repository.getNextWords(windowStart + words.size, batchSize)
                if (newWords.isNotEmpty()) {
                    val updatedWords = words + newWords
                    if (updatedWords.size > windowSize) {
                        val removeCount = updatedWords.size - windowSize
                        words.removeRange(0, removeCount)
                        windowStart += removeCount
                    }
                    words.addAll(newWords)
                } else {
                    hasMoreWords = false
                }
            } catch (e: Exception) {
                hasMoreWords = false
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadPreviousWords() {
        viewModelScope.launch {
            isLoading = true
            try {
                val prevBatchStart = (windowStart - batchSize).coerceAtLeast(0)
                val prevWords = repository.getNextWords(prevBatchStart, batchSize)
                if (prevWords.isNotEmpty()) {
                    val updatedWords = prevWords + words
                    words.clear()
                    words.addAll(updatedWords.take(windowSize))
                    windowStart = prevBatchStart
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }
}
