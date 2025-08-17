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

    // Track if we have a limited word set (like only 1 word)
    private var isLimitedWordSet = false
    private var totalAvailableWords = 0

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

                // Check if we have limited words
                totalAvailableWords = initialWords.size
                if (totalAvailableWords < windowSize) {
                    isLimitedWordSet = true
                    hasMoreWords = false // No more words to load
                }
            } catch (e: Exception) {
                hasMoreWords = false
            } finally {
                isLoading = false
            }
        }
    }

    fun onPageChanged(currentPage: Int) {
        // If we have a limited word set, use circular navigation
        if (isLimitedWordSet && words.isNotEmpty()) {
            // No need to load more words, just cycle through existing ones
            return
        }

        val relativeIndex = currentPage - windowStart
        if (relativeIndex >= words.size - threshold && !isLoading && hasMoreWords) {
            loadNextWords()
        } else if (relativeIndex <= threshold && windowStart > 0 && !isLoading) {
            loadPreviousWords()
        }
    }

    private fun loadNextWords() {
        if (isLimitedWordSet) return // Don't load more if we have limited set

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
                    // Check if we now have a limited set
                    if (words.size < windowSize) {
                        isLimitedWordSet = true
                        totalAvailableWords = words.size
                    }
                }
            } catch (e: Exception) {
                hasMoreWords = false
                if (words.size < windowSize) {
                    isLimitedWordSet = true
                    totalAvailableWords = words.size
                }
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadPreviousWords() {
        if (isLimitedWordSet) return // Don't load more if we have limited set

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

    // Helper function to get the word for infinite paging
    fun getWordForPage(page: Int): Word? {
        if (words.isEmpty()) return null

        return if (isLimitedWordSet) {
            // Cycle through available words
            val wordIndex = page % words.size
            words[wordIndex]
        } else {
            // Use sliding window logic
            val relativeIndex = page - windowStart
            if (relativeIndex in words.indices) {
                words[relativeIndex]
            } else {
                null
            }
        }
    }

    // Helper function to check if we should show loading for a page
    fun shouldShowLoading(page: Int): Boolean {
        if (isLimitedWordSet) {
            return false // Never show loading for limited word sets
        }

        val relativeIndex = page - windowStart
        return relativeIndex !in words.indices
    }
}