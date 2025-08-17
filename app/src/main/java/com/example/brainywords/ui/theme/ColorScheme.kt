package com.example.brainywords.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val backgroundGradient: Brush,
    val highlightColor: Color
)

object ColorSchemes {
    private val purpleScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFDCDC),
                Color(0xFFFFEFEF)
            )
        ),
        highlightColor = Color(0xFFD0433C)
    )

    private val blueScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFDCFFF7),
                Color(0xFFEFFEF3)
            )
        ),
        highlightColor = Color(0xFF2CA88F)
    )

    private val greenScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFE3D6),
                Color(0xFFFFF5EC)
            )
        ),
        highlightColor = Color(0xFFE87A53)
    )

    private val orangeScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE6D9FF),
                Color(0xFFF8ECFF)
            )
        ),
        highlightColor = Color(0xFF8C55D9)
    )

    private val tealScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFD6F2FF),
                Color(0xFFECFAFF)
            )
        ),
        highlightColor = Color(0xFF3A9FD9)
    )

    private val indigoScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFF6D6),
                Color(0xFFFFFBEA)
            )
        ),
        highlightColor = Color(0xFFE6B534)
    )

    private val redScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFD6FFFB),
                Color(0xFFE7FFF4)
            )
        ),
        highlightColor = Color(0xFF2CA6C9)
    )

    private val pinkScheme = ColorScheme(
        backgroundGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD6E5),
                Color(0xFFFFEEF5)
            )
        ),
        highlightColor = Color(0xFFD94376)
    )

    private val allSchemes = listOf(
        purpleScheme,
        blueScheme,
        greenScheme,
        orangeScheme,
        tealScheme,
        indigoScheme,
        redScheme,
        pinkScheme,
    )

    fun getRandomScheme(): ColorScheme {
        return allSchemes.random()
    }

    fun getSchemeForIndex(index: Int): ColorScheme {
        return allSchemes[index % allSchemes.size]
    }
}