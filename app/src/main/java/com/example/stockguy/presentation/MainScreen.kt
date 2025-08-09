package com.example.stockguy.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text as WearText
import com.example.stockguy.data.ApiService
import com.example.stockguy.data.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val pairs = listOf("SOLUSDT", "BTCUSDT", "ETHUSDT", "BNBUSDT")

    var selectedPair by remember { mutableStateOf(pairs.first()) }
    var result by remember { mutableStateOf("Select a pair and press Fetch") }

    val scope = rememberCoroutineScope()

    // Carrega par salvo no DataStore
    LaunchedEffect(Unit) {
        DataStoreManager.getSelectedPair(context).collect { savedPair ->
            selectedPair = savedPair
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WearText(text = "Selected: $selectedPair")

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = {
                    val currentIndex = pairs.indexOf(selectedPair)
                    selectedPair = pairs[(currentIndex - 1 + pairs.size) % pairs.size]
                    scope.launch {
                        DataStoreManager.saveSelectedPair(context, selectedPair)
                    }
                }) {
                    WearText("<")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val currentIndex = pairs.indexOf(selectedPair)
                    selectedPair = pairs[(currentIndex + 1) % pairs.size]
                    scope.launch {
                        DataStoreManager.saveSelectedPair(context, selectedPair)
                    }
                }) {
                    WearText(">")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                scope.launch {
                    try {
                        val ticker = ApiService.api.getTicker24h(selectedPair)
                        result = "Price: ${ticker.lastPrice}\nChange: ${ticker.priceChangePercent}%"
                    } catch (e: Exception) {
                        result = "Error: ${e.message}"
                    }
                }
            }) {
                WearText("Fetch")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = result)
        }
    }
}
