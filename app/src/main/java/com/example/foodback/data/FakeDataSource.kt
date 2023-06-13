package com.example.foodback.data

import com.example.foodback.data.remote.response.Exercise
import com.example.foodback.data.remote.response.Meal

object FakeDataSource {
    val dummyDiary = mutableListOf(
        "Breakfast",
//        Meal("makanan1", 1, 1, "date"),
//        Meal("makanan1", "desc1"),
//        AddButton(true,"Add Breakfast"),
//        "Lunch",
//        Meal("makanan2", "desc2"),
//        Meal("makanan2", "desc2"),
//        FakeAddButton(true,"Add Lunch"),
//        "Snack",
//        Meal("makanan3", "desc3"),
//        Meal("makanan3", "desc3"),
//        FakeAddButton(true,"Add Snack"),
//        "Dinner",
//        Meal("makanan4", "desc4"),
//        Meal("makanan4", "desc4"),
//        FakeAddButton(true, "Add Dinner"),
        "Exercises",
//        Exercise(1, "exercise1", 1, 1, 1, 1, 1, "date",  "ca", "ua"),
//        Meal("aktivitas5", "desc5"),
//        AddButton(false, "Add Exercise"),
    )

    val dummyAdd = mutableListOf(
        FakeAddData("Add 1", "Desc 1"),
        FakeAddData("Add 2", "Desc 2"),
        FakeAddData("Add 3", "Desc 3"),
        FakeAddData("Add 4", "Desc 4"),
        FakeAddData("Add 5", "Desc 5"),
        FakeAddData("Add 6", "Desc 6"),
        FakeAddData("Add 7", "Desc 7"),
        FakeAddData("Add 8", "Desc 8"),
        FakeAddData("Add 9", "Desc 9"),
        FakeAddData("Add 10", "Desc 10"),
    )
}