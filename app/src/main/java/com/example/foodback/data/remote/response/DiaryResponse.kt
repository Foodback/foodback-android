package com.example.foodback.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiaryResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val diaryData: DiaryData,
)

data class DiaryData(
    @field:SerializedName("meals")
    val listDataMeal: List<DataMeal>,

    @field:SerializedName("exercises")
    val dataExercise: DataExercise,
)

data class DataMeal(
    @field:SerializedName("label")
    val label: String,

    @field:SerializedName("totalCalories")
    val totalCalories: Long,

    @field:SerializedName("meals")
    val listMeals: List<Meal>,
)

data class Meal(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("amount")
    val amount: Long,

    @field:SerializedName("calories")
    val calories: Long,

    @field:SerializedName("date")
    val date: String
)

data class DataExercise(
    @field:SerializedName("exercises")
    val listExercises: List<Exercise>,

    @field:SerializedName("totalCalories")
    val totalCalories: Long,
)

data class Exercise(
    @field:SerializedName("id")
    val id: Long,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: Long,

    @field:SerializedName("calories")
    val calories: Long,

    @field:SerializedName("duration")
    val duration: Long,

    @field:SerializedName("sets")
    val sets: Long,

    @field:SerializedName("repetition")
    val repetition: Long,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,
)

data class AddMealResponse(
    @field:SerializedName("message")
    val message: String,
)

data class AddExerciseResponse(
    @field:SerializedName("message")
    val message: String,
)