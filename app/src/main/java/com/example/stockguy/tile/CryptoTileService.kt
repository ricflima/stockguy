package com.example.stockguy.tile

import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.example.stockguy.data.BinanceApi
import com.example.stockguy.data.DataStoreManager
import com.example.stockguy.data.TickerResponse
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalHorologistApi::class)
@AndroidEntryPoint
class CryptoTileService : SuspendingTileService() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var binanceApi: BinanceApi

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        return try {
            val selectedPair = dataStoreManager.getSelectedPair().first()
            val response = binanceApi.getTicker24h(selectedPair)

            if (response.isSuccessful && response.body() != null) {
                createSuccessTile(response.body()!!, selectedPair)
            } else {
                createErrorTile(selectedPair, "API Error")
            }
        } catch (e: Exception) {
            createErrorTile("SOL", "Network Error")
        }
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ) = ResourceBuilders.Resources.Builder()
        .setVersion("1")
        .build()

    private fun createSuccessTile(ticker: TickerResponse, symbol: String): TileBuilders.Tile {
        val changeColor = if (ticker.isPositiveChange()) {
            ColorBuilders.argb(0xFF4CAF50.toInt()) // Green
        } else {
            ColorBuilders.argb(0xFFF44336.toInt()) // Red
        }

        val cryptoName = ticker.getCryptoName()
        val changeIcon = if (ticker.isPositiveChange()) "📈" else "📉"

        val layout = PrimaryLayout.Builder(
            DeviceParametersBuilders.DeviceParameters.Builder().build()
        )
            .setResponsiveContentInsetEnabled(true)
            .setPrimaryLabelText(cryptoName) // Only crypto name (SOL, BTC, etc.)
            .setContent(
                LayoutElementBuilders.Column.Builder()
                    .addContent(
                        Text.Builder(this, "💰 ${ticker.getFormattedPrice()}")
                            .setColor(ColorBuilders.argb(Colors.DEFAULT.onSurface))
                            .setTypography(Typography.TYPOGRAPHY_TITLE3)
                            .build()
                    )
                    .addContent(
                        Text.Builder(this, "$changeIcon ${ticker.getFormattedChangePercent()}")
                            .setColor(changeColor)
                            .setTypography(Typography.TYPOGRAPHY_BODY1)
                            .build()
                    )
                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setFreshnessIntervalMillis(60_000) // 1 minute
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(layout)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun createErrorTile(symbol: String, error: String): TileBuilders.Tile {
        val layout = PrimaryLayout.Builder(
            DeviceParametersBuilders.DeviceParameters.Builder().build()
        )
            .setResponsiveContentInsetEnabled(true)
            .setPrimaryLabelText(symbol)
            .setContent(
                Text.Builder(this, "❌ $error")
                    .setColor(ColorBuilders.argb(0xFFF44336.toInt()))
                    .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(layout)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }
}