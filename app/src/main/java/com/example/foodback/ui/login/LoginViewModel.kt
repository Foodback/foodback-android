package com.example.foodback.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.foodback.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun isLogin() = repository.isLogin().asLiveData()

    fun login(email: String, password: String) = repository.login(email, password).asLiveData()

    fun register(email: String, password: String) = repository.register(email, password).asLiveData()

    fun logout() = repository.logout().asLiveData()
}