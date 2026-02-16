package com.example.foodshunter.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodshunter.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val isLoggedIn: Boolean
        get() = repository.currentUser != null

    /**
     * Login dengan username atau email
     */
    fun login(usernameOrEmail: String, password: String) {
        if (usernameOrEmail.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Username/Email dan password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repository.login(usernameOrEmail, password)

            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                val errorMessage = when {
                    result.exceptionOrNull()?.message?.contains("Username tidak ditemukan") == true ->
                        "Username tidak ditemukan"
                    result.exceptionOrNull()?.message?.contains("password is invalid") == true ->
                        "Password salah"
                    result.exceptionOrNull()?.message?.contains("no user record") == true ->
                        "Email tidak terdaftar"
                    result.exceptionOrNull()?.message?.contains("badly formatted") == true ->
                        "Format email tidak valid"
                    else -> "Login gagal: ${result.exceptionOrNull()?.message}"
                }
                AuthState.Error(errorMessage)
            }
        }
    }

    /**
     * Register user baru dengan username
     */
    fun register(email: String, password: String, name: String, username: String) {
        // Validation
        if (name.isBlank()) {
            _authState.value = AuthState.Error("Nama tidak boleh kosong")
            return
        }
        if (username.isBlank()) {
            _authState.value = AuthState.Error("Username tidak boleh kosong")
            return
        }
        if (username.length < 3) {
            _authState.value = AuthState.Error("Username minimal 3 karakter")
            return
        }
        if (username.contains(" ")) {
            _authState.value = AuthState.Error("Username tidak boleh mengandung spasi")
            return
        }
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Email tidak boleh kosong")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password minimal 6 karakter")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repository.register(email, password, name, username)

            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                val errorMessage = when {
                    result.exceptionOrNull()?.message?.contains("Username sudah digunakan") == true ->
                        "Username sudah digunakan, pilih yang lain"
                    result.exceptionOrNull()?.message?.contains("email address is already in use") == true ->
                        "Email sudah terdaftar"
                    result.exceptionOrNull()?.message?.contains("badly formatted") == true ->
                        "Format email tidak valid"
                    else -> "Registrasi gagal: ${result.exceptionOrNull()?.message}"
                }
                AuthState.Error(errorMessage)
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}