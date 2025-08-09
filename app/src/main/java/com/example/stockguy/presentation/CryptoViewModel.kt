package com.example.stockguy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockguy.data.CryptoRepository
import com.example.stockguy.data.DataStoreManager
import com.example.stockguy.data.TickerResponse
import com.example.stockguy.data.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _selectedPair = MutableStateFlow("SOLUSDT")
    val selectedPair: StateFlow<String> = _selectedPair.asStateFlow()

    private val _tickerData = MutableStateFlow<UiState<TickerResponse>>(UiState.Idle)
    val tickerData: StateFlow<UiState<TickerResponse>> = _tickerData.asStateFlow()

    private val _availableSymbols = MutableStateFlow<List<String>>(emptyList())
    val availableSymbols: StateFlow<List<String>> = _availableSymbols.asStateFlow()

    private var autoUpdateJob: Job? = null

    init {
        loadInitialData()
        startAutoUpdate()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Load saved pair
            _selectedPair.value = dataStoreManager.getSelectedPair().first()

            // Load available symbols
            try {
                val symbols = repository.getAvailableSymbols()
                _availableSymbols.value = symbols
            } catch (e: Exception) {
                _availableSymbols.value = repository.getDefaultSymbols()
            }

            // Initial fetch
            fetchData()
        }
    }

    fun selectPair(pair: String) {
        viewModelScope.launch {
            _selectedPair.value = pair
            dataStoreManager.saveSelectedPair(pair)
            fetchData()
        }
    }

    fun refreshData() {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _tickerData.value = UiState.Loading

            try {
                val ticker = repository.getTicker(_selectedPair.value)
                _tickerData.value = UiState.Success(ticker)
            } catch (e: Exception) {
                _tickerData.value = UiState.Error(e)
            }
        }
    }

    private fun startAutoUpdate() {
        autoUpdateJob?.cancel()
        autoUpdateJob = viewModelScope.launch {
            while (true) {
                delay(30_000) // Update every 30 seconds
                if (_tickerData.value is UiState.Success || _tickerData.value is UiState.Error) {
                    fetchData()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoUpdateJob?.cancel()
    }
}