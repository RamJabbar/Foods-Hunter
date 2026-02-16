package com.example.foodshunter.data.repository

import com.example.foodshunter.data.model.CartItem
import com.example.foodshunter.data.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepository{
private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

fun addToCart(menuItem: MenuItem, restaurantName: String) {
    val currentItems = _cartItems.value.toMutableList()

    val existingItem = currentItems.find {
        it.menuItem.id == menuItem.id
    }

    if (existingItem != null) {
        val index = currentItems.indexOf(existingItem)
        currentItems[index] = existingItem.copy(
            quantity = existingItem.quantity + 1
        )
    } else {
        currentItems.add(
            CartItem(
                menuItem = menuItem,
                quantity = 1,
                restaurantName = restaurantName
            )
        )
    }

    _cartItems.value = currentItems
}

    fun removeFromCart(cartitem : CartItem){
        _cartItems.value = cartItems.value.filter{
            it.menuItem.id != cartitem.menuItem.id
        }
    }
    fun updateQuantity(cartItem: CartItem, newQuantity: Int){
        if(newQuantity <= 0) {
            removeFromCart(cartItem)
            return
        }
    val currentItems = _cartItems.value.toMutableList()
    val index = currentItems.indexOfFirst { it.menuItem.id == cartItem.menuItem.id}

        if (index != -1){
            currentItems[index] = cartItem.copy(quantity = newQuantity)
        _cartItems.value = currentItems
        }
    }
    fun clearCart(){
        _cartItems.value = emptyList()

    }
    fun getTotalPrice(): Double{
        return _cartItems.value.sumOf { it.totalPrice }

    }
    fun getItemCount(): Int{
        return _cartItems.value.sumOf { it.quantity }

    }
}