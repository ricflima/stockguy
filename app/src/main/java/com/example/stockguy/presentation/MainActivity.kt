package com.example.stockguy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.stockguy.presentation.theme.StockGuyTheme
import com.example.stockguy.tile.CryptoTileUpdateWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CryptoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule periodic tile updates
        schedulePeriodicTileUpdates()

        setContent {
            StockGuyTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }

    private fun schedulePeriodicTileUpdates() {
        val workRequest = PeriodicWorkRequestBuilder<CryptoTileUpdateWorker>(
            15, TimeUnit.MINUTES, // Minimum allowed for PeriodicWork
            5, TimeUnit.MINUTES   // Flex interval
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "crypto_tile_update",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
}