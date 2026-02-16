package com.example.foodshunter.data.model

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int = 1,
    val restaurantName: String = ""
){
    val totalPrice: Double
        get() = menuItem.price * quantity
}
