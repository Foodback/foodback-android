package com.example.foodback.data

import android.util.Log
import com.example.foodback.data.local.AuthPreferences
import com.example.foodback.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class Repository private constructor(private val apiService: ApiService, private val authPreferences: AuthPreferences){

    fun isLogin(): Flow<String?> = flow { emitAll(authPreferences.getToken()) }

    fun login(email: String, password: String) = flow{
        authPreferences.saveToken(FakeDataSource.dummyToken)
        emit(Result.Success("success"))
    }

    fun register(name: String, email: String, password: String) = flow{
        emit(Result.Success("A"))
    }

    fun logout(): Flow<Result<String>> = flow {
        emit(Result.Loading)
        authPreferences.logout()
        emit(Result.Success("success"))
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            authPreferences: AuthPreferences,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, authPreferences)
            }.also { instance = it }
    }
}