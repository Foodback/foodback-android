package com.example.foodback.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodback.data.HomeModel
import com.example.foodback.data.Repository
import com.example.foodback.data.Result
import com.example.foodback.data.remote.response.ProfileResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, val date: String) : ViewModel() {

    private val _homeData = MutableLiveData<Result<HomeModel>>()
    val homeData : LiveData<Result<HomeModel>>
        get() = _homeData

    private val _diaryData = MutableLiveData<Result<List<Any>>>()
    val diaryData : LiveData<Result<List<Any>>>
        get() = _diaryData

    private val _profileData = MutableLiveData<Result<ProfileResponse>>()
    val profileData : LiveData<Result<ProfileResponse>>
        get() = _profileData

    init {
        getHome()
        getDiary(date)
        getProfile()
    }

    fun getHome(){
        viewModelScope.launch {
            repository.getHome(date).collect{
                _homeData.value = it
            }
        }
    }

    fun getDiary(startDate: String){
        viewModelScope.launch {
            repository.getDiary(startDate).collect{
                _diaryData.value = it
            }
        }
    }

    fun getProfile(){
        viewModelScope.launch {
            repository.getProfile().collect{
                _profileData.value = it
            }
        }

    }
}