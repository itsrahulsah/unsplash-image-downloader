package com.sample.hackitdemo.di

import android.content.Context
import androidx.room.Room
import com.sample.hackitdemo.database.AppDatabase
import com.sample.hackitdemo.network.api.ApiService
import com.sample.hackitdemo.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideWallpaperRepository(apiService: ApiService):ImageRepository{
        return ImageRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db.db"
        ).build()
    }
}