package com.example.brainywords.data.model

data class Word(
    val id: Int = 0,
    val word: String,
    val definition: String,
    val synonyms: List<String>,
    val quote: String,
    val author: String,
    val source: String
)