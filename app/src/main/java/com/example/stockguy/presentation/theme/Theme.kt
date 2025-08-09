package com.example.stockguy.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun StockGuyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}