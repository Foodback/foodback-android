package com.example.foodback.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodback.data.Repository
import com.example.foodback.data.Result
import com.example.foodback.data.remote.response.ExerciseResponse
import kotlinx.coroutines.launch

class ExerciseViewModel(private val repository: Repository) : ViewModel() {

    private val _exerciseData = MutableLiveData<Result<ExerciseResponse>>()
    val exerciseData : LiveData<Result<ExerciseResponse>>
        get() = _exerciseData

    private var _date = String()
    val date: String
        get() = _date

    init {
        getExercise()
    }

    fun setDate(date: String){
        _date = date
    }

    fun getExercise(query: String = ""){
        viewModelScope.launch {
            repository.getExercise(query).collect{
                _exerciseData.value = it
            }
        }
    }

    fun addExercise(name: String, calories: Long, duration: Long, sets: Long, repetition: Long, date: String) = repository.addExercise(name, calories, duration, sets, repetition, date).asLiveData()
}