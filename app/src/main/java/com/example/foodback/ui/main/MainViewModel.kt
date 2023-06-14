package com.example.foodback.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodback.data.Repository
import com.example.foodback.data.Result
import com.example.foodback.data.remote.response.DiaryResponse
import com.example.foodback.data.remote.response.HomeResponse
import com.example.foodback.data.remote.response.ProfileData
import com.example.foodback.data.remote.response.ProfileResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, val date: String) : ViewModel() {

    private val _homeData = MutableLiveData<Result<HomeResponse>>()
    val homeData : LiveData<Result<HomeResponse>>
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
        Log.i("TEST", "$date")
    }

    fun getHome(){
        viewModelScope.launch {
            repository.getHome().collect{
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