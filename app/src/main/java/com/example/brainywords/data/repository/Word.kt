package com.example.brainywords.data.repository

import com.example.brainywords.data.model.Word
import com.example.brainywords.data.remote.WordRemoteDataSource

class WordRepository(
    private val remote: WordRemoteDataSource
) {
    suspend fun getInitialWords(batchSize: Int = 15): List<Word> {
        return remote.fetchWords(startIndex = 0, batchSize = batchSize)
    }

    suspend fun getNextWords(offset: Int, batchSize: Int = 5): List<Word> {
        return remote.fetchWords(startIndex = offset, batchSize = batchSize)
    }
}
