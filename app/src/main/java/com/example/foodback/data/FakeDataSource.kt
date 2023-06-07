package com.example.foodback.data

object FakeDataSource {
    val dummyDiary = mutableListOf(
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