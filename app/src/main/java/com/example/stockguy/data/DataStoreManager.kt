package com.example.stockguy.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("crypto_prefs")

object DataStoreManager {
    private val SELECTED_PAIR_KEY = stringPreferencesKey("selected_pair")

    suspend fun saveSelectedPair(context: Context, pair: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_PAIR_KEY] = pair
        }
    }

    fun getSelectedPair(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[SELECTED_PAIR_KEY] ?: "SOLUSDT" // Valor padrão
        }
    }
}