package com.example.stockguy.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TickerResponse(
    @SerializedName("symbol")
    val symbol: String = "",

    @SerializedName("lastPrice")
    val lastPrice: String = "0",

    @SerializedName("priceChangePercent")
    val priceChangePercent: String = "0",

    @SerializedName("priceChange")
    val priceChange: String = "0",

    @SerializedName("openTime")
    val openTime: Long = 0L,

    @SerializedName("closeTime")
    val closeTime: Long = 0L
) {
    fun getFormattedPrice(): String {
        return try {
            val price = lastPrice.toDouble()
            when {
                price >= 1 -> "$${"%.2f".format(price)}"
                price >= 0.01 -> "$${"%.4f".format(price)}"
                else -> "$${"%.6f".format(price)}"
            }
        } catch (e: Exception) {
            "$0.00"
        }
    }

    fun getFormattedChangePercent(): String {
        return try {
            val change = priceChangePercent.toDouble()
            val formatted = "%.2f".format(change)
            "${if (change >= 0) "+" else ""}$formatted%"
        } catch (e: Exception) {
            "0.00%"
        }
    }

    fun isPositiveChange(): Boolean {
        return try {
            priceChangePercent.toDouble() >= 0
        } catch (e: Exception) {
            false
        }
    }

    fun getChangeColor(): Int {
        return if (isPositiveChange()) 0xFF4CAF50.toInt() else 0xFFF44336.toInt()
    }

    fun getCryptoName(): String {
        return symbol.replace("USDT", "").replace("USD", "")
    }
}