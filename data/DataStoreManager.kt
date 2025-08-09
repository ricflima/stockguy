package com.example.stockguy.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

object DataStoreManager {
    private val PAIR_KEY = stringPreferencesKey("selected_pair")

    fun getSelectedPair(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[PAIR_KEY] ?: "BTCUSDT"
        }
    }

    suspend fun saveSelectedPair(context: Context, pair: String) {
        context.dataStore.edit { prefs ->
            prefs[PAIR_KEY] = pair
        }
    }
}