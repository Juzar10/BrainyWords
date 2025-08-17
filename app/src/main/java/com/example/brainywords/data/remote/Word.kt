package com.example.brainywords.data.remote

import com.example.brainywords.data.model.Word
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class WordRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val wordsCollection = firestore.collection("Words")

    suspend fun fetchWords(startIndex: Int, batchSize: Int): List<Word> {
        val snapshot = wordsCollection
            .orderBy("id", Query.Direction.ASCENDING)
            .startAfter(startIndex - 1) // startAfter is exclusive
            .limit(batchSize.toLong())
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Word(
                id = doc.getLong("id")?.toInt() ?: 0,
                word = doc.getString("word") ?: "",
                definition = doc.getString("definition") ?: "",
                synonyms = doc.get("synonyms") as? List<String> ?: emptyList(),
                quote = doc.getString("quote") ?: "",
                author = doc.getString("author") ?: "",
                source = doc.getString("source") ?: "",
                viewCount = doc.getLong("viewCount")?.toInt() ?: 0
            )
        }
    }

    suspend fun incrementViewCount(wordId: Int): Boolean {
        return try {
            // Find the document with the matching word ID
            val querySnapshot = wordsCollection
                .whereEqualTo("id", wordId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val document = querySnapshot.documents.first()

                // Increment the viewCount field
                document.reference.update("viewCount", FieldValue.increment(1)).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}