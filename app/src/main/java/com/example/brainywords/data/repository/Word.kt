package com.example.brainywords.data.repository

import com.example.brainywords.data.model.Word
import com.example.brainywords.data.remote.WordRemoteDataSource

class WordRepository(
    private val remoteDataSource: WordRemoteDataSource
) {
    private val _words = mutableListOf<Word>()
    val words: List<Word> get() = _words

    suspend fun fetchWords(startIndex: Int, batchSize: Int): List<Word> {
        val newWords = remoteDataSource.fetchWords(startIndex, batchSize)
        _words.addAll(newWords)
        return newWords
    }

    suspend fun incrementViewCount(wordId: Int): Boolean {
        val success = remoteDataSource.incrementViewCount(wordId)

        if (success) {
            // Update local cache as well
            val wordIndex = _words.indexOfFirst { it.id == wordId }
            if (wordIndex != -1) {
                val updatedWord = _words[wordIndex].copy(
                    viewCount = _words[wordIndex].viewCount + 1
                )
                _words[wordIndex] = updatedWord
            }
        }

        return success
    }
}