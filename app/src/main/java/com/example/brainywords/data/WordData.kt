package com.example.brainywords.data
import kotlinx.coroutines.delay

data class Word(
    val id: Int = 0,
    val word: String,
    val definition: String,
    val synonyms: List<String>,
    val quote: String,
    val author: String,
    val source: String
)

object WordData {
    private const val BATCH_SIZE = 5

    // Generate a dynamic word based on index
    private fun generateWord(index: Int): Word {
        val wordNumber = index + 1
        return Word(
            id = index,
            word = "Word $wordNumber",
            definition = "This is the definition for Word $wordNumber",
            synonyms = listOf("Synonym1", "Synonym2", "Synonym3", "Synonym4", "Synonym5"),
            quote = "This is a sample quote for Word $wordNumber",
            author = "Sample Author",
            source = "This is source for all"
        )
    }

    // Simulated API call - replace with actual network/database call
    suspend fun getInitialBatch(): List<Word> {
        delay(500) // Simulate network delay
        return (0 until BATCH_SIZE).map { index ->
            generateWord(index)
        }
    }

    // Fetch next batch starting from offset - this will generate infinite words
    suspend fun getNextBatch(offset: Int): List<Word> {
        delay(1000) // Simulate network delay

        // Generate next batch of words starting from offset
        return (offset until offset + BATCH_SIZE).map { index ->
            generateWord(index)
        }
    }

    // Synchronous version for immediate access
    fun getInitialBatchSync(): List<Word> {
        return (0 until BATCH_SIZE).map { index ->
            generateWord(index)
        }
    }

    // Non-suspend version that can be called directly
    fun getNextBatchSync(offset: Int): List<Word> {
        // Generate next batch of words starting from offset
        return (offset until offset + BATCH_SIZE).map { index ->
            generateWord(index)
        }
    }

    // Public method to generate a word at a specific index (for sliding window)
    fun generateWordAtIndex(index: Int): Word {
        return generateWord(index)
    }
}