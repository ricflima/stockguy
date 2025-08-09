package com.example.stockguy.tile

import android.graphics.Color
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileProviderService
import androidx.wear.tiles.material.Text
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.example.stockguy.data.ApiService
import com.example.stockguy.data.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CryptoTileService : TileProviderService() {

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        var price = "--"
        var change = "--"
        var selectedPair = "SOLUSDT"
        var changeColor = Color.WHITE

        runBlocking {
            selectedPair = DataStoreManager.getSelectedPair(applicationContext).first()
            try {
                val ticker = ApiService.api.getTicker24h(selectedPair)
                price = ticker.lastPrice
                change = ticker.priceChangePercent

                val changeValue = change.toDoubleOrNull() ?: 0.0
                changeColor = if (changeValue >= 0) Color.GREEN else Color.RED

            } catch (_: Exception) { }
        }

        val layout = PrimaryLayout.Builder()
            .setPrimaryLabelText(selectedPair)
            .setContent(
                Text.Builder()
                    .setText("💰 $price\n${if (changeColor == Color.GREEN) "📈" else "📉"} $change%")
                    .setColor(changeColor)
                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setTileTimeline(
                TileBuilders.TileTimeline.Builder()
                    .addTimelineEntry(
                        TileBuilders.TimelineEntry.Builder()
                            .setLayout(
                                TileBuilders.Layout.Builder()
                                    .setRoot(layout)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        androidx.wear.tiles.ResourceBuilders.Resources.Builder()
            .setVersion("1")
            .build()
}