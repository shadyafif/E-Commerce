package com.example.shopping.data.di

import android.app.Application
import androidx.room.Room
import com.example.shopping.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, "E-commerceDataBase")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDao(db: AppDatabase) = db.getRoomDao()
}