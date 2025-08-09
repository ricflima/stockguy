package com.example.stockguy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockguy.data.DataStoreManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val pairs = listOf("BTCUSDT", "ETHUSDT", "SOLUSDT", "ADAUSDT")
            var selected by remember { mutableStateOf("BTCUSDT") }

            LaunchedEffect(Unit) {
                DataStoreManager.getSelectedPair(this@MainActivity).collect {
                    selected = it
                }
            }

            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Escolha o par de criptos", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(pairs.size) { index ->
                            val pair = pairs[index]
                            Button(
                                onClick = {
                                    scope.launch {
                                        DataStoreManager.saveSelectedPair(this@MainActivity, pair)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (pair == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(pair)
                            }
                        }
                    }
                }
            }
        }
    }
}