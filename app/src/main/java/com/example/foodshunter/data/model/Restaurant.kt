package com.example.foodshunter.data.model

data class Restaurant(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val category: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val isPopular: Boolean = false
)