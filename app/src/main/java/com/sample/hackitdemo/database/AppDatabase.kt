package com.sample.hackitdemo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.hackitdemo.database.dao.WallpaperDao
import com.sample.hackitdemo.database.models.LikedWallpaper

@Database(entities = [LikedWallpaper::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun getWallpaperDao():WallpaperDao
}