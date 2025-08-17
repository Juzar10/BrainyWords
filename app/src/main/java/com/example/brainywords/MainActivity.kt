package com.example.brainywords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainywords.data.WordData
import com.example.brainywords.ui.common.BottomNavigationBar
import com.example.brainywords.ui.screens.WordScreen
import com.example.brainywords.ui.theme.BrainyWordsTheme
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
    val words = WordData.words
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { words.size })
    val coroutineScope = rememberCoroutineScope();

    val currentWordIndex = pagerState.currentPage
    val colorScheme = ColorSchemes.getSchemeForIndex(currentWordIndex)

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
                    totalWords = words.size,
                    colorScheme = colorScheme,
                    onPrevious = {
                        if (currentWordIndex > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentWordIndex - 1)
                            }
                        }
                    },
                    onNext = {
                        if (currentWordIndex < words.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentWordIndex + 1)
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(innerPadding)
            ) { index ->
                WordScreen(
                    word = words[index],
                    colorScheme = ColorSchemes.getSchemeForIndex(index),
                    modifier = Modifier.fillMaxSize()
                )
            }
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