package com.example.foodback.data

import android.util.Log
import com.example.foodback.R
import com.example.foodback.data.local.AuthPreferences
import com.example.foodback.data.remote.response.DataExercise
import com.example.foodback.data.remote.response.DataMeal
import com.example.foodback.data.remote.retrofit.ApiService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class Repository private constructor(private val apiService: ApiService, private val authPreferences: AuthPreferences){

    private val auth = Firebase.auth

    fun isLogin() = flow {
        if(auth.currentUser != null){
            val token: String = auth.currentUser!!.getIdToken(true).await().token!!
            authPreferences.saveToken(token)
        }
        emit(auth.currentUser)
//        emitAll(authPreferences.getToken())
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

    fun register(email: String, password: String, data: Map<String, String>) = flow{
        emit(Result.Loading)
        try {
            val target = data[TARGET_KEY]?.toLong()
            val result = apiService.addProfile(
                name = data[NAME_KEY]!!,
                email = email,
                gender = data[GENDER_KEY]!!,
                height = 160,
                weight = 55,
                activity = data[LEVEL_KEY]!!,
                goal = data[GOAL_KEY]!!,
                target = target?:0,
            )
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(Result.Success(result))
        }catch (e: Exception){
            Log.d("Repository", "register: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getHome() = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val result = apiService.getHome(it)
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "getHome: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDiary(startDate: String) = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{ token ->
                if (token != null) {
                    val result = apiService.getDiary(token, startDate, startDate)
                    val dataBreakfast = result.diaryData.listDataMeal.firstOrNull { it.label == "breakfast"}
                    val dataLunch = result.diaryData.listDataMeal.firstOrNull { it.label == "lunch"}
                    val dataDinner = result.diaryData.listDataMeal.firstOrNull { it.label == "dinner"}
                    val dataExercise = result.diaryData.dataExercise
                    val data = setData(dataBreakfast, dataLunch, dataDinner, dataExercise)
                    emit(Result.Success(data))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "getDiary: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    private fun setData(dataBreakfast: DataMeal?, dataLunch: DataMeal?, dataDinner: DataMeal?, dataExercise: DataExercise?): List<Any> {
        val adapter = mutableListOf<Any>()
        adapter.add(DiaryType(dataBreakfast?.totalCalories?:0,"Breakfast", R.drawable.sunrise))
        dataBreakfast?.listMeals?.forEach{ adapter.add(it) }
        adapter.add(AddButton(true, "Add Breakfast", "Breakfast"))

        adapter.add(DiaryType(dataLunch?.totalCalories?:0,"Lunch",R.drawable.sun))
        dataLunch?.listMeals?.forEach{ adapter.add(it) }
        adapter.add(AddButton(true, "Add Lunch", "Lunch"))

        adapter.add(DiaryType(dataDinner?.totalCalories?:0,"Dinner",R.drawable.moon))
        dataDinner?.listMeals?.forEach{ adapter.add(it) }
        adapter.add(AddButton(true, "Add Dinner", "Dinner"))

        adapter.add(DiaryType(dataExercise?.totalCalories?:0,"Exercise",R.drawable.exercise))
        dataExercise?.listExercises?.forEach{ adapter.add(it) }
        adapter.add(AddButton(false, "Add Exercise", "Exercise"))
        return adapter
    }


    fun getFood(query: String) = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val result = apiService.getFood(it, query)
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "getFood: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addMeal(label: String, name: String, amount: Long, calories: Long, date: String) = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val userId = apiService.getProfile(it).profileData.id
                    val result = apiService.addMeal(
                        token = it,
                        userId = userId,
                        label = label,
                        name = name,
                        amount = amount,
                        calories = calories,
                        date = date,
                    )
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "addMeal: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getExercise(query: String) = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val result = apiService.getExercise(it, query)
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "addExercise: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    fun addExercise(name: String, calories: Long, duration: Long, sets: Long, repetition: Long, date: String) = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val userId = apiService.getProfile(it).profileData.id
                    val result = apiService.addExercise(
                        token = it,
                        userId = userId,
                        name = name,
                        calories = calories,
                        duration = duration,
                        sets = sets,
                        repetition = repetition,
                        date = date,
                    )
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "addMeal: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getProfile() = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val result = apiService.getProfile(it)
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "getProfile: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun editProfile(name: String, email: String, gender: String, height: Long, weight: Long, activity: String, goal:String, target: Long) = flow {
        emit(Result.Loading)
        try{
            authPreferences.getToken().collect{
                if (it != null) {
                    val id = apiService.getProfile(it).profileData.id
                    val result = apiService.editProfile(
                        token = it,
                        id = id,
                        name = name,
                        email = email,
                        gender = gender,
                        height = height,
                        weight = weight,
                        activity = activity,
                        goal = goal,
                        target = target,
                    )
                    emit(Result.Success(result))
                }
            }
        }catch (e: Exception){
            Log.d("Repository", "getProfile: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun logout() = flow {
        emit(Result.Loading)
        try{
            auth.signOut()
            authPreferences.logout()
            emit(Result.Success("Logout Success"))
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

        private const val GOAL_KEY: String = "goal"
        private const val GENDER_KEY: String = "gender"
        private const val HEIGHT_KEY = "height"
        private const val WEIGHT_KEY = "weight"
        private const val LEVEL_KEY = "level"
        private const val TARGET_KEY = "target"
        private const val NAME_KEY = "name"
    }
}