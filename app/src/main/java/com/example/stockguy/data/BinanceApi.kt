package com.example.stockguy.data

import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    suspend fun getTicker24h(@Query("symbol") symbol: String): TickerResponse
}