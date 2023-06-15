package com.example.foodback.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AddProfileResponse(
    @field:SerializedName("message")
    val message: String,
)

data class EditProfileResponse(
    @field:SerializedName("message")
    val message: String,
)

data class ProfileResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val profileData: ProfileData,
)

@Parcelize
data class ProfileData(
    @field:SerializedName("id")
    val id: Long,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("height")
    val height: Long,

    @field:SerializedName("weight")
    val weight: Long,

    @field:SerializedName("activity")
    val activity: String,

    @field:SerializedName("goal")
    val goal: String,

    @field:SerializedName("target")
    val target: Long,

    @field:SerializedName("age")
    val age: Long,
): Parcelable



