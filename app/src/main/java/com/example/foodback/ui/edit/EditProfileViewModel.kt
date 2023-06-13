package com.example.foodback.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.foodback.data.Repository

class EditProfileViewModel(private val repository: Repository) : ViewModel() {

    fun editProfile(name: String, email: String, gender: String, height: Long, weight: Long, activity: String, goal:String, target: Long) = repository.editProfile(name, email, gender, height, weight, activity, goal, target).asLiveData()

}