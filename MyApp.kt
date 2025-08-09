package com.example.stockguy

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.stockguy.tile.CryptoTileUpdateWorker
import java.util.concurrent.TimeUnit

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val workRequest = PeriodicWorkRequestBuilder<CryptoTileUpdateWorker>(
            1, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CryptoTileUpdater",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}