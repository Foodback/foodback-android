package com.example.foodback.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.foodback.data.local.AuthPreferences
import com.example.foodback.data.Repository
import com.example.foodback.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(dataStore: DataStore<Preferences>): Repository {
        val apiService = ApiConfig.getApiService()
        val authPreferences = AuthPreferences.getInstance(dataStore)
        return Repository.getInstance(apiService, authPreferences)
    }
}