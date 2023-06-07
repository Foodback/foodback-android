package com.example.foodback.data

data class FakeData(
    val name: String,
    val desc: String,
)

data class FakeAddButton(
    val isFood: Boolean,
    val name: String,
)

data class FakeAddData(
    val name: String,
    val desc: String,
)

//data class FakeFood(
//    val name: String,
//    val desc: String,
//    val type: String,
//)

data class FakeBreakfast(
    val type: String,
    val desc: String,
)

data class FakeLunch(
    val type: String,
    val desc: String,
)