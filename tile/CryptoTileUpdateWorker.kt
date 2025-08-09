package com.example.stockguy.tile

import android.content.Context
import androidx.wear.tiles.TileUpdateRequester
import androidx.wear.tiles.TileUpdateRequesterFactory
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.wear.tiles.ComponentName

class CryptoTileUpdateWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val requester: TileUpdateRequester =
            TileUpdateRequesterFactory.create(applicationContext)
        requester.requestUpdate(
            ComponentName(
                applicationContext.packageName,
                CryptoTileService::class.java.name
            )
        )
        return Result.success()
    }
}