package com.example.foodback.data.remote.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val homeData: HomeData,
)

data class HomeData(
    @field:SerializedName("target")
    val target: Long,

    @field:SerializedName("goal")
    val goal: String,

    @field:SerializedName("foodCalories")
    val foodCalories: Long,

    @field:SerializedName("exerciseCalories")
    val exerciseCalories: Long,
)