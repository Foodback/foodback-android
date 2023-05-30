package com.example.foodback.data

object FakeDataSource {
    val dummyDiary = mutableListOf<Any>(
        "Breakfast",
        FakeData("makanan1", "desc1"),
        FakeData("makanan1", "desc1"),
        FakeAddButton(true,"Add Breakfast"),
        "Lunch",
        FakeData("makanan2", "desc2"),
        FakeData("makanan2", "desc2"),
        FakeAddButton(true,"Add Lunch"),
        "Snack",
        FakeData("makanan3", "desc3"),
        FakeData("makanan3", "desc3"),
        FakeAddButton(true,"Add Snack"),
        "Dinner",
        FakeData("makanan4", "desc4"),
        FakeData("makanan4", "desc4"),
        FakeAddButton(true, "Add Dinner"),
        "Exercises",
        FakeData("aktivitas5", "desc5"),
        FakeData("aktivitas5", "desc5"),
        FakeAddButton(false, "Add Exercise"),
    )
}