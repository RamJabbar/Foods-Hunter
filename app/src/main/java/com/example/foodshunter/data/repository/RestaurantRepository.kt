package com.example.foodshunter.data.repository

import com.example.foodshunter.data.model.MenuItem
import com.example.foodshunter.data.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class RestaurantRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getPopularRestaurants(): Result<List<Restaurant>> {
    return try {
        val snapshot = firestore.collection("restaurants")
            .whereEqualTo("isPopular", true)
            .get()
            .await()

        val restaurants = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Restaurant::class.java)?.copy(id = doc.id)
        }

        Result.success(restaurants)
    }catch (e: Exception) {
        Result.failure(e)
    }
  }
    suspend fun getRestaurantById(id: String): Result<Restaurant> {
        return try {
            val document = firestore.collection("restaurants")
                .document(id)
                .get()
                .await()

            val restaurant = document.toObject(Restaurant::class.java)
                ?: throw Exception("Restaurant not found")

            Result.success(restaurant)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getMenuItems(restaurantId: String): Result<List<MenuItem>> {
        return try {
            val snapshot = firestore.collection("menuItems")
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await()

            val menuItems = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MenuItem::class.java)?.copy(id = doc.id)
            }

            Result.success(menuItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun calculateDistance(
        lat1: Double, lan1: Double,
        lat2 :Double, lan2: Double
    ): Double{
    val earthRadius = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lan2 - lan1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2)* sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}

