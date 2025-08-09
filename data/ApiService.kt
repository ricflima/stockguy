package com.example.stockguy.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class TickerResponse(
    val symbol: String,
    val lastPrice: String,
    val priceChangePercent: String
)

interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    suspend fun getTicker24h(@Query("symbol") symbol: String): TickerResponse
}

object ApiService {
    val api: BinanceApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.binance.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinanceApi::class.java)
    }
}