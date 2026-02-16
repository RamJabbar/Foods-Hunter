package com.example.foodshunter.data.repository


import com.example.foodshunter.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Register new user dengan username
     */
    suspend fun register(
        email: String,
        password: String,
        name: String,
        username: String  // ← PARAMETER BARU
    ): Result<User> {
        return try {
            // Check apakah username sudah dipakai
            val usernameExists = checkUsernameExists(username)
            if (usernameExists) {
                return Result.failure(Exception("Username sudah digunakan"))
            }

            // Create Firebase Auth user
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("User creation failed")

            // Create User object dengan username
            val user = User(
                uid = firebaseUser.uid,
                email = email,
                name = name,
                username = username,  // ← TAMBAHAN BARU
                createdAt = System.currentTimeMillis()
            )

            // Save to Firestore
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login dengan username atau email
     */
    suspend fun login(usernameOrEmail: String, password: String): Result<FirebaseUser> {
        return try {
            // Check apakah input adalah email atau username
            val email = if (usernameOrEmail.contains("@")) {
                // Input adalah email
                usernameOrEmail
            } else {
                // Input adalah username - cari email dari Firestore
                getEmailFromUsername(usernameOrEmail)
                    ?: throw Exception("Username tidak ditemukan")
            }

            // Login dengan email
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Login failed")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logout current user
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Get user data from Firestore
     */
    suspend fun getUserData(uid: String): Result<User> {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(User::class.java)
                ?: throw Exception("User not found")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check apakah username sudah dipakai
     */
    private suspend fun checkUsernameExists(username: String): Boolean {
        return try {
            val result = firestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()

            !result.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get email dari username
     */
    private suspend fun getEmailFromUsername(username: String): String? {
        return try {
            val result = firestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()

            if (result.isEmpty) {
                null
            } else {
                result.documents[0].toObject(User::class.java)?.email
            }
        } catch (e: Exception) {
            null
        }
    }
}