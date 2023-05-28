package com.example.foodback.data.remote.response

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: String
)