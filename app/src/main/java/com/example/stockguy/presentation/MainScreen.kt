package com.example.stockguy.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.stockguy.data.TickerResponse
import com.example.stockguy.data.UiState
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults

@Composable
fun MainScreen(viewModel: CryptoViewModel) {
    val selectedPair by viewModel.selectedPair.collectAsState()
    val tickerData by viewModel.tickerData.collectAsState()
    val availableSymbols by viewModel.availableSymbols.collectAsState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 32.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 32.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        scalingParams = ScalingLazyColumnDefaults.scalingParams()
    ) {
        // Header
        item {
            Text(
                text = "Crypto Tracker",
                style = MaterialTheme.typography.title3,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )
        }

        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Current Selection Display
        item {
            CurrentPairCard(
                pair = selectedPair,
                tickerData = tickerData,
                onRefresh = { viewModel.refreshData() }
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Quick Navigation (Previous/Next)
        item {
            if (availableSymbols.isNotEmpty()) {
                QuickNavigationButtons(
                    currentPair = selectedPair,
                    availableSymbols = availableSymbols,
                    onPairSelected = { viewModel.selectPair(it) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Available Symbols List
        if (availableSymbols.isNotEmpty()) {
            items(availableSymbols) { pair ->
                CryptoPairChip(
                    pair = pair,
                    isSelected = pair == selectedPair,
                    onClick = { viewModel.selectPair(pair) }
                )
            }
        }
    }
}

@Composable
private fun CurrentPairCard(
    pair: String,
    tickerData: UiState<TickerResponse>,
    onRefresh: () -> Unit
) {
    Card(
        onClick = onRefresh,
        modifier = Modifier.fillMaxWidth(),
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = MaterialTheme.colors.surface,
            endBackgroundColor = MaterialTheme.colors.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pair.replace("USDT", ""),
                style = MaterialTheme.typography.title2,
                color = MaterialTheme.colors.primary
            )

            when (tickerData) {
                is UiState.Loading -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "Updating...",
                        style = MaterialTheme.typography.caption1,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                is UiState.Success -> {
                    val ticker = tickerData.data
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ticker.getFormattedPrice(),
                        style = MaterialTheme.typography.title3,
                        color = Color(ticker.getChangeColor())
                    )
                    Text(
                        text = ticker.getFormattedChangePercent(),
                        style = MaterialTheme.typography.body1,
                        color = Color(ticker.getChangeColor())
                    )
                    Text(
                        text = "Tap to refresh",
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                is UiState.Error -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Error loading data",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Tap to retry",
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                is UiState.Idle -> {
                    Text(
                        text = "Tap to fetch data",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickNavigationButtons(
    currentPair: String,
    availableSymbols: List<String>,
    onPairSelected: (String) -> Unit
) {
    val currentIndex = availableSymbols.indexOf(currentPair)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                val prevIndex = if (currentIndex <= 0) availableSymbols.size - 1 else currentIndex - 1
                onPairSelected(availableSymbols[prevIndex])
            },
            modifier = Modifier.size(40.dp)
        ) {
            Text("◀")
        }

        Text(
            text = "${currentIndex + 1}/${availableSymbols.size}",
            style = MaterialTheme.typography.caption1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
        )

        Button(
            onClick = {
                val nextIndex = if (currentIndex >= availableSymbols.size - 1) 0 else currentIndex + 1
                onPairSelected(availableSymbols[nextIndex])
            },
            modifier = Modifier.size(40.dp)
        ) {
            Text("▶")
        }
    }
}

@Composable
private fun CryptoPairChip(
    pair: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Chip(
        onClick = onClick,
        label = {
            Text(
                text = pair.replace("USDT", ""),
                style = MaterialTheme.typography.button
            )
        },
        modifier = Modifier.fillMaxWidth(0.85f),
        colors = ChipDefaults.chipColors(
            backgroundColor = if (isSelected)
                MaterialTheme.colors.primary
            else
                MaterialTheme.colors.surface,
            contentColor = if (isSelected)
                MaterialTheme.colors.onPrimary
            else
                MaterialTheme.colors.onSurface
        )
    )
}