package com.example.brainywords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainywords.data.Word
import com.example.brainywords.data.WordData
import com.example.brainywords.ui.common.BottomNavigationBar
import com.example.brainywords.ui.screens.WordScreen
import com.example.brainywords.ui.theme.BrainyWordsTheme
import com.example.brainywords.ui.theme.ColorScheme
import com.example.brainywords.ui.theme.ColorSchemes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrainyWordsTheme {
                WordApp()
            }
        }
    }
}

@Composable
fun WordApp() {
    // State to hold dynamically loaded words with sliding window
    var words by remember { mutableStateOf<List<Word>>(emptyList()) }
    var windowStart by remember { mutableStateOf(0) } // Track the starting index of our window
    var isLoading by remember { mutableStateOf(true) }
    var hasMoreWords by remember { mutableStateOf(true) }

    val windowSize = 15 // Keep only 15 words in memory at a time
    val batchSize = 5   // Load 5 words at a time

    // Load initial batch when composable is first created
    LaunchedEffect(Unit) {
        try {
            val initialWords = WordData.getInitialBatch()
            words = initialWords
            windowStart = 0
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
            hasMoreWords = false
        }
    }

    val maxPages = if (hasMoreWords) Int.MAX_VALUE else windowStart + words.size
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { maxPages }
    )
    val coroutineScope = rememberCoroutineScope()

    val currentWordIndex = pagerState.currentPage

    // Calculate which word to show from our sliding window
    val actualWordIndex = if (words.isNotEmpty()) {
        val relativeIndex = currentWordIndex - windowStart
        if (relativeIndex >= 0 && relativeIndex < words.size) {
            relativeIndex
        } else {
            // Handle edge cases
            maxOf(0, minOf(relativeIndex, words.size - 1))
        }
    } else 0

    val colorScheme = ColorSchemes.getSchemeForIndex(actualWordIndex)

    // Sliding window management
    LaunchedEffect(currentWordIndex, words.size) {
        if (words.isEmpty()) return@LaunchedEffect

        val relativeIndex = currentWordIndex - windowStart
        val threshold = 3

        // Load more words if approaching the end of current window
        if (relativeIndex >= words.size - threshold && !isLoading && hasMoreWords) {
            coroutineScope.launch {
                isLoading = true
                try {
                    val newWords = WordData.getNextBatchSync(windowStart + words.size)
                    if (newWords.isNotEmpty()) {
                        val updatedWords = words + newWords

                        // Implement sliding window: remove old words if we exceed window size
                        if (updatedWords.size > windowSize) {
                            val wordsToRemove = updatedWords.size - windowSize
                            words = updatedWords.drop(wordsToRemove)
                            windowStart += wordsToRemove
                        } else {
                            words = updatedWords
                        }

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

        // Load previous words if going backward and we're near the beginning
        else if (relativeIndex <= threshold && windowStart > 0 && !isLoading) {
            coroutineScope.launch {
                isLoading = true
                try {
                    val prevBatchStart = maxOf(0, windowStart - batchSize)
                    val prevWords = (prevBatchStart until windowStart).map { index ->
                        WordData.generateWordAtIndex(index)
                    }

                    if (prevWords.isNotEmpty()) {
                        val updatedWords = prevWords + words

                        // Maintain window size by removing from the end
                        if (updatedWords.size > windowSize) {
                            words = updatedWords.take(windowSize)
                        } else {
                            words = updatedWords
                        }
                        windowStart = prevBatchStart
                    }
                } catch (e: Exception) {
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // Rest of your UI code remains the same...
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.backgroundGradient)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                BottomNavigationBar(
                    currentIndex = currentWordIndex,
                    totalWords = -1, // Always infinite with sliding window
                    colorScheme = colorScheme,
                    onPrevious = {
                        if (currentWordIndex > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentWordIndex - 1)
                            }
                        }
                    },
                    onNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(currentWordIndex + 1)
                        }
                    },
                )
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(innerPadding)
            ) { index ->
                if (words.isEmpty()) {
                    LoadingScreen(colorScheme = colorScheme)
                } else {
                    val relativeIndex = index - windowStart
                    if (relativeIndex >= 0 && relativeIndex < words.size) {
                        WordScreen(
                            word = words[relativeIndex],
                            colorScheme = ColorSchemes.getSchemeForIndex(relativeIndex),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Show loading or placeholder for words outside current window
                        LoadingScreen(colorScheme = colorScheme)
                    }
                }
            }
        }
    }
}
@Composable
fun LoadingScreen(
    colorScheme: ColorScheme,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading words...",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordAppPreview() {
    BrainyWordsTheme {
        WordApp()
    }
}