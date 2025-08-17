package com.example.brainywords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainywords.ui.theme.BrainyWordsTheme
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.brainywords.data.Word
import com.example.brainywords.data.WordData
import com.example.brainywords.ui.theme.ColorScheme
import com.example.brainywords.ui.theme.ColorSchemes
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

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
    var currentWordIndex by remember { mutableIntStateOf(0) }
    val words = WordData.words
    val currentWord = words[currentWordIndex]
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
                            currentWordIndex--
                        }
                    },
                    onNext = {
                        if (currentWordIndex < words.size - 1) {
                            currentWordIndex++
                        }
                    }
                )
            }
        ) { innerPadding ->
            WordScreen(
                word = currentWord,
                colorScheme = colorScheme,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentIndex: Int,
    totalWords: Int,
    colorScheme: ColorScheme,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = 12.dp,
                    start = 12.dp,
                    top = 12.dp,
                    bottom = 24.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Arrow
            IconButton(
                onClick = onPrevious,
                enabled = currentIndex > 0
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    tint = if (currentIndex > 0)
                        Color.Black
                    else
                        Color.Black.copy(alpha = 0.38f)
                )
            }

            // Center Text
            Text(
                text = "${currentIndex + 1} / $totalWords",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )

            // Right Arrow
            IconButton(
                onClick = onNext,
                enabled = currentIndex < totalWords - 1
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = if (currentIndex < totalWords - 1)
                        Color.Black
                    else
                        Color.Black.copy(alpha = 0.38f)
                )
            }
        }
    }
}

// Add this helper function to build the annotated string
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

@Composable
fun WordScreen(
    word: Word,
    colorScheme: ColorScheme,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
            ),
        verticalArrangement = Arrangement.Center
    ) {
        // Quote Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 32.dp,
                    top = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.quote),
                contentDescription = "Quote",
                tint = colorScheme.highlightColor
            )
            Text(
                text = buildHighlightedQuote(word.quote, word.word, colorScheme.highlightColor),
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = word.author.uppercase(),
                    fontSize = 14.sp,
                )
                Text(
                    text = "- ${word.source}",
                    fontSize = 14.sp,
                )
            }
        }

        // Word Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 32.dp,
                    top = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = word.word,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = word.definition,
                fontSize = 16.sp,
            )
            SynonymSection(
                synonyms = word.synonyms,
                highlightColor = colorScheme.highlightColor
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SynonymSection(
    synonyms: List<String>,
    highlightColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Synonyms",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            synonyms.forEach { synonym ->
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Transparent,
                    modifier = Modifier.border(
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(6.dp)
                    )
                ) {
                    Text(
                        text = synonym,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                    )
                }
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