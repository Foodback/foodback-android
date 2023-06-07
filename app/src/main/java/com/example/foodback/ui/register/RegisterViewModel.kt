package com.example.foodback.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.foodback.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    private val _data: MutableMap<String, String> = mutableMapOf()
    val data: Map<String, String>
        get() = _data

    fun addData(key: String, data: String){
        _data[key] = data
    }

    fun register(email: String, password: String) = repository.register(email, password).asLiveData()
}