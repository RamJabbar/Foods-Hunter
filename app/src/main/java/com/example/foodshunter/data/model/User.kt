package com.example.foodshunter.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val username: String = "",
    val createdAt: Long = System.currentTimeMillis()
)