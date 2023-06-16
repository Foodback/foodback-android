package com.example.foodback.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AddButton(
    val isFood: Boolean,
    val hint: String,
    val label: String,
)

data class DiaryType(
    val calorie: Long,
    val name: String,
    val image: Int
)

data class HomeModel(
    val target: Long,
    val goal: String,
    val foodCalories: Long,
    val exerciseCalories: Long,
    val calorieNeeds: Long,
)

@Parcelize
data class DetailModel  (
    val name: String,
    val calorie: Long,
    val fat: String,
    val cholesterol: String,
    val sugar: String,
    val protein: String,
    val calcium: String,
    val iron: String,
): Parcelable