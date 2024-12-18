package com.dicoding.picodiploma.loginwithanimation.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
class StoryEntity (

    @PrimaryKey
    val id: String,

    val photoUrl: String,

    val createdAt: String,

    val name: String,

    val description: String,

    val lat : Double = 0.0,
    val lon : Double = 0.0
)