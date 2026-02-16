package com.example.foodshunter.ui.screens.cart

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.foodshunter.data.model.CartItem
import com.example.foodshunter.data.repository.CartRepository
import kotlinx.coroutines.flow.StateFlow

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        cartRepository.updateQuantity(cartItem, newQuantity)
    }

    fun removeItem(cartItem: CartItem) {
        cartRepository.removeFromCart(cartItem)
    }
    fun clearCart(){
        cartRepository.clearCart()
    }
    fun getTotalPrice(): Double{
        return cartRepository.getTotalPrice()
    }
    fun getItemCount(): Int{
        return cartRepository.getItemCount()
    }




}