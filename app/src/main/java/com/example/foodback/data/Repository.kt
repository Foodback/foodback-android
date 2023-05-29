package com.example.foodback.data

import android.content.Intent
import android.util.Log
import com.example.foodback.data.local.AuthPreferences
import com.example.foodback.data.remote.retrofit.ApiService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class Repository private constructor(private val apiService: ApiService, private val authPreferences: AuthPreferences){

    private val auth = Firebase.auth

    fun isLogin() = flow {

//        emit(auth.currentUser)

        emitAll(authPreferences.getToken())
    }

    fun login(email: String, password: String) = flow{
        emit(Result.Loading)
        try{
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val token = auth.currentUser!!.getIdToken(true).await().token!!
            authPreferences.saveToken(token)
            emit(Result.Success(result))
        }catch (e: Exception){
            Log.d("Repository", "login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(email: String, password: String) = flow{
        emit(Result.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(Result.Success(result))
        }catch (e: Exception){
            Log.d("Repository", "register: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun logout(): Flow<Result<String>> = flow {
//        emit(Result.Loading)
//        authPreferences.logout()
//        emit(Result.Success("success"))
//    }

    fun logout() = flow {
        emit(Result.Loading)
        try{
            auth.signOut()
            authPreferences.logout()
            emit(Result.Success("success"))
        }catch (e: Exception){
            Log.d("Repository", "logout: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
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