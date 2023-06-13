package com.example.foodback.data.remote.retrofit

import com.example.foodback.data.remote.response.AddMealResponse
import com.example.foodback.data.remote.response.AddProfileResponse
import com.example.foodback.data.remote.response.DiaryResponse
import com.example.foodback.data.remote.response.EditProfileResponse
import com.example.foodback.data.remote.response.ExerciseResponse
import com.example.foodback.data.remote.response.FoodResponse
import com.example.foodback.data.remote.response.HomeResponse
import com.example.foodback.data.remote.response.ProfileResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("home")
    suspend fun getHome(
        @Header("Authorization") token: String,
    ): HomeResponse

    @GET("diary")
    suspend fun getDiary(
        @Header("Authorization") token: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): DiaryResponse

    @GET("food")
    suspend fun getFood(
        @Header("Authorization") token: String,
        @Query("query") query: String,
    ): FoodResponse

    @FormUrlEncoded
    @POST("diary/meal")
    suspend fun addMeal(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Long,
        @Field("label") label: String,
        @Field("name") name: String,
        @Field("amount") amount: Long,
        @Field("calories") calories: Long,
        @Field("date") date: String,
    ): AddMealResponse

    @GET("exercise")
    suspend fun getExercise(
        @Header("Authorization") token: String,
        @Query("query") query: String,
    ): ExerciseResponse

    @FormUrlEncoded
    @POST("diary/exercise")
    suspend fun addExercise(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Long,
        @Field("name") name: String,
        @Field("calories") calories: Long,
        @Field("duration") duration: Long,
        @Field("sets") sets: Long,
        @Field("repetition") repetition: Long,
        @Field("date") date: String,
    ): AddMealResponse

    @FormUrlEncoded
    @POST("profile")
    suspend fun addProfile(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("height") height: Long,
        @Field("weight") weight: Long,
        @Field("activity") activity: String,
        @Field("goal") goal: String,
        @Field("target") target: Long,
    ): AddProfileResponse

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @FormUrlEncoded
    @PUT("profile/{id}")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("height") height: Long,
        @Field("weight") weight: Long,
        @Field("activity") activity: String,
        @Field("goal") goal: String,
        @Field("target") target: Long,
    ): EditProfileResponse

}