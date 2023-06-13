package com.example.foodback.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodback.data.Repository
import com.example.foodback.data.Result
import com.example.foodback.data.remote.response.ExerciseResponse
import com.example.foodback.data.remote.response.FoodResponse
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: Repository) : ViewModel() {

    private val _foodData = MutableLiveData<Result<FoodResponse>>()
    val foodData : LiveData<Result<FoodResponse>>
        get() = _foodData

    private var _date = String()
    val date: String
        get() = _date

    private var _label = String()
    val label: String
        get() = _label

    init {
        getFood()
    }

    fun setDate(date: String){
        _date = date
    }

    fun setLabel(label: String){
        _label = label
    }

    fun getFood(query: String = ""){
        viewModelScope.launch {
            repository.getFood(query).collect{
                _foodData.value = it
            }
        }
    }

    fun addMeal(label: String, name: String, amount: Long, calories: Long, date: String) = repository.addMeal(label, name, amount, calories, date).asLiveData()

}