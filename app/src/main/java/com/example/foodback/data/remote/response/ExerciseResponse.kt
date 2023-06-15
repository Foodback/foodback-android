package com.example.foodback.data.remote.response

import com.google.gson.annotations.SerializedName

data class ExerciseResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val exerciseData: List<ExerciseData>,
)

data class ExerciseData (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("calories_per_hour")
    val calories_per_hour: Long,

    @field:SerializedName("duration_minutes")
    val duration_minutes: Long,

    @field:SerializedName("total_calories")
    val total_calories: Long,
)