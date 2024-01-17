package com.sample.hackitdemo.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedWallpaper(
    @PrimaryKey(autoGenerate = false)
    val id:String,
    val isLiked:Boolean,
    val uri:String,
    val description:String
)
