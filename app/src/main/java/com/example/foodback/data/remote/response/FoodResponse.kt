package com.example.foodback.data.remote.response

import com.google.gson.annotations.SerializedName

data class FoodResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val foodData: FoodData,
)

data class FoodData (
    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("menuItems")
    val menuItems: List<MenuItems>,
)

data class MenuItems (
    @field:SerializedName("id")
    val id: Long,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("price")
    val price: String?,

    @field:SerializedName("likes")
    val likes: Long,

    @field:SerializedName("nutrition")
    val nutrition: Nutrition,

    @field:SerializedName("images")
    val images: List<String>,

    @field:SerializedName("image")
    val image: String,
)

data class Nutrition (
    @field:SerializedName("calories")
    val calories: Float,

    @field:SerializedName("fat")
    val fat: String,

    @field:SerializedName("protein")
    val protein: String,

    @field:SerializedName("carbs")
    val carbs: String,
)

