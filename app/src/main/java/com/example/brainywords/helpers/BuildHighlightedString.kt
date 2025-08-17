package com.example.brainywords.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


@Composable
fun buildHighlightedQuote(quote: String, targetWord: String, highlightColor: Color): AnnotatedString {
    return buildAnnotatedString {
        append("\"")

        val words = quote.split(" ")
        words.forEachIndexed { index, word ->
            // Remove punctuation for comparison but keep it for display
            val cleanWord = word.replace(Regex("[^a-zA-Z]"), "").lowercase()
            val cleanTargetWord = targetWord.replace(Regex("[^a-zA-Z]"), "").lowercase()

            if (cleanWord == cleanTargetWord) {
                // Highlight the matching word
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = highlightColor
                    )
                ) {
                    append(word)
                }
            } else {
                // Regular word
                append(word)
            }

            // Add space between words (except for the last word)
            if (index < words.size - 1) {
                append(" ")
            }
        }

        append("\"")
    }
}
