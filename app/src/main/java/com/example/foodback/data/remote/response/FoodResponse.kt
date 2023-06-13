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

//    @field:SerializedName("badges")
//    val badges: Badges,

    @field:SerializedName("nutrition")
    val nutrition: Nutrition,

    @field:SerializedName("images")
    val images: List<String>,

//    @field:SerializedName("servings")
//    val servings: String,

//    @field:SerializedName("spoonacularScore")
//    val spoonacularScore: String?,

//    @field:SerializedName("breadcrumbs")
//    val breadcrumbs: String,

    @field:SerializedName("image")
    val image: String,

//    @field:SerializedName("imageType")
//    val imageType: String,

//    @field:SerializedName("generatedText")
//    val generatedText: String,

//    @field:SerializedName("restaurantChain")
//    val restaurantChain: String,
)

data class Nutrition (
//    @field:SerializedName("nutrients")
//    val listNutrients: List<Nutrients>,
//
//    @field:SerializedName("caloricBreakdown")
//    val caloricBreakdown: CaloricBreakdown,

    @field:SerializedName("calories")
    val calories: Long,

    @field:SerializedName("fat")
    val fat: String,

    @field:SerializedName("protein")
    val protein: String,

    @field:SerializedName("carbs")
    val carbs: String,
)

data class Nutrients (
//    @field:SerializedName("name")
//    val name: String,

    @field:SerializedName("amount")
    val amount: Long,

//    @field:SerializedName("unit")
//    val unit: String,
//
//    @field:SerializedName("percentOfDailyNeeds")
//    val percentOfDailyNeeds: Long,
)

//data class CaloricBreakdown (
//    @field:SerializedName("percentProtein")
//    val percentProtein: Long,
//
//    @field:SerializedName("percentFat")
//    val percentFat: Long,
//
//    @field:SerializedName("percentCarbs")
//    val percentCarbs: Long,
//)

