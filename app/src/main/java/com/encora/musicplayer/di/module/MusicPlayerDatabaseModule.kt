package com.encora.musicplayer.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.encora.musicplayer.data.local.MusicPlayerDatabase
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MusicPlayerDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = MusicPlayerDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideSongsDao(database: MusicPlayerDatabase) = database.getSongsDao()
}
