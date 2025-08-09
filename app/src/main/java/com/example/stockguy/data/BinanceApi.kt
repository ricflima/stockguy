package com.example.stockguy.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    suspend fun getTicker24h(@Query("symbol") symbol: String): Response<TickerResponse>

    @GET("api/v3/exchangeInfo")
    suspend fun getExchangeInfo(): Response<ExchangeInfoResponse>
}

data class ExchangeInfoResponse(
    val symbols: List<SymbolInfo>
)

data class SymbolInfo(
    val symbol: String,
    val status: String
)