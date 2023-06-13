package com.example.foodback.data

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