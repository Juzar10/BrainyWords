package com.example.brainywords.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainywords.R
import com.example.brainywords.data.Word
import com.example.brainywords.helpers.buildHighlightedQuote
import com.example.brainywords.ui.componenets.SynonymSection
import com.example.brainywords.ui.theme.ColorScheme

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