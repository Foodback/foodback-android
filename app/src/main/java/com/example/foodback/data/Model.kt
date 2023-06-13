package com.example.foodback.data

data class FakeData(
    val name: String,
    val desc: String,
)

data class AddButton(
    val isFood: Boolean,
    val hint: String,
    val label: String,
)

data class FakeAddData(
    val name: String,
    val desc: String,
)