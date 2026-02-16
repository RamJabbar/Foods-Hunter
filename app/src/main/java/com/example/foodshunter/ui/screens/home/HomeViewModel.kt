package com.example.fooddeliveryapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshunter.data.model.Restaurant
import com.example.foodshunter.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val restaurants: List<Restaurant>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {
    private val repository = RestaurantRepository()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPopularRestaurants()
    }

    fun loadPopularRestaurants() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val result = repository.getPopularRestaurants()

            _uiState.value = if (result.isSuccess) {
                val restaurants = result.getOrNull() ?: emptyList()
                HomeUiState.Success(restaurants)
            } else {
                HomeUiState.Error(
                    result.exceptionOrNull()?.message ?: "Gagal memuat restaurant"
                )
            }
        }
    }

    fun refresh() {
        loadPopularRestaurants()
    }
}