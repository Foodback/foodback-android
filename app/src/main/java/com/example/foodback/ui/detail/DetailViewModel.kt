package com.example.foodback.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.foodback.data.Repository

class DetailViewModel(private val repository: Repository) : ViewModel() {

    fun addMeal(label: String, name: String, amount: Long, calories: Long, date: String) = repository.addMeal(label, name, amount, calories, date).asLiveData()
}