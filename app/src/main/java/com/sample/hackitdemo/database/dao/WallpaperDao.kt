package com.sample.hackitdemo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sample.hackitdemo.database.models.LikedWallpaper

@Dao
interface WallpaperDao {
    @Query("SELECT * FROM LikedWallpaper")
    suspend fun getLikedWallpapers():List<LikedWallpaper>
}