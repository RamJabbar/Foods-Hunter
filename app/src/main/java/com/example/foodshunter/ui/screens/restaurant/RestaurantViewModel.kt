package com.example.foodshunter.ui.screens.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshunter.data.model.MenuItem
import com.example.foodshunter.data.model.Restaurant
import com.example.foodshunter.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RestaurantDetailState(
    val restaurant: Restaurant? = null,
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val distance: Double? = null // Tambahkan properti distance
)
class RestaurantViewModel : ViewModel() {
    private val repository = RestaurantRepository()

    private val _uiState = MutableStateFlow(RestaurantDetailState())
    val uiState: StateFlow<RestaurantDetailState> = _uiState.asStateFlow()

    /**
     * Load restaurant dan menu berdasarkan ID
     */
    fun loadRestaurantDetail(restaurantId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load restaurant
            val restaurantResult = repository.getRestaurantById(restaurantId)
            if (restaurantResult.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal memuat detail restaurant"
                )
                return@launch
            }

            val restaurant = restaurantResult.getOrNull()!!

            // Load menu items
            val menuResult = repository.getMenuItems(restaurantId)
            val menuItems = menuResult.getOrNull() ?: emptyList()

            _uiState.value = _uiState.value.copy(
                restaurant = restaurant,
                menuItems = menuItems,
                isLoading = false
            )
        }
    }

    /**
     * Calculate dan update jarak dari user location
     */
    fun updateDistance(userLat: Double, userLon: Double) {
        val restaurant = _uiState.value.restaurant ?: return

        val distance = repository.calculateDistance(
            userLat, userLon,
            restaurant.latitude, restaurant.longitude
        )

        _uiState.value = _uiState.value.copy(distance = distance)
    }

}