package com.example.foodback.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.foodback.data.Repository

class AuthViewModel(private val repository: Repository) : ViewModel() {

    fun isLogin() = repository.isLogin().asLiveData()

    fun login(email: String, password: String) = repository.login(email, password).asLiveData()

    fun register(name: String, email: String, password: String) = repository.register(name, email, password).asLiveData()
}