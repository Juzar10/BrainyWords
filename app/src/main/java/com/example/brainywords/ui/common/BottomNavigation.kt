package com.example.brainywords.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainywords.ui.theme.ColorScheme

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
