package com.example.stockguy.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore("crypto_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val SELECTED_PAIR_KEY = stringPreferencesKey("selected_pair")

    suspend fun saveSelectedPair(pair: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_PAIR_KEY] = pair
        }
    }

    fun getSelectedPair(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[SELECTED_PAIR_KEY] ?: "SOLUSDT"
        }
    }
}