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
import com.example.brainywords.data.remote.WordRemoteDataSource
import com.example.brainywords.data.repository.WordRepository
import com.example.brainywords.domain.viewmodals.WordViewModel
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
    // Repository
    val repository = remember { WordRepository(WordRemoteDataSource()) }
    val viewModel = remember { WordViewModel(repository) }

    val words = viewModel.words
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { Int.MAX_VALUE })
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage

    // Notify ViewModel when page changes
    LaunchedEffect(currentPage) {
        viewModel.onPageChanged(currentPage)
    }

    // Determine the color scheme based on current word
    val colorScheme = if (words.isNotEmpty()) {
        ColorSchemes.getSchemeForIndex(currentPage % words.size)
    } else {
        ColorSchemes.getSchemeForIndex(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.backgroundGradient)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                // Get the current word to access its viewCount
                val currentWord = viewModel.getWordForPage(currentPage)

                BottomNavigationBar(
                    currentIndex = currentPage,
                    colorScheme = colorScheme,
                    viewCount = currentWord?.viewCount ?: 0, // Get viewCount from current word
                    onPrevious = {
                        if (currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentPage - 1)
                            }
                        }
                    },
                    onNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    },
                )
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(innerPadding)
            ) { page ->
                val word = viewModel.getWordForPage(page)
                val shouldShowLoading = viewModel.shouldShowLoading(page)

                when {
                    word != null -> {
                        WordScreen(
                            word = word,
                            colorScheme = ColorSchemes.getSchemeForIndex(page % words.size),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    shouldShowLoading -> {
                        LoadingScreen(
                            colorScheme = colorScheme,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        // Fallback for any edge cases
                        LoadingScreen(
                            colorScheme = colorScheme,
                            modifier = Modifier.fillMaxSize()
                        )
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