package com.example.stockguy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepository @Inject constructor(
    private val api: BinanceApi
) {
    suspend fun getTicker(symbol: String): TickerResponse {
        return withContext(Dispatchers.IO) {
            val response = api.getTicker24h(symbol)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("API Error: ${response.code()}")
            }
        }
    }

    suspend fun getAvailableSymbols(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getExchangeInfo()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.symbols
                        .filter { it.status == "TRADING" && it.symbol.endsWith("USDT") }
                        .map { it.symbol }
                        .take(20) // Limit to top 20
                } else {
                    getDefaultSymbols()
                }
            } catch (e: Exception) {
                getDefaultSymbols()
            }
        }
    }

    private fun getDefaultSymbols(): List<String> {
        return listOf(
            "SOLUSDT", "BTCUSDT", "ETHUSDT", "BNBUSDT",
            "ADAUSDT", "DOTUSDT", "LINKUSDT", "XRPUSDT"
        )
    }
}