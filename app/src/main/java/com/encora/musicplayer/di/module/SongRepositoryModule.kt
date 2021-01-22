package com.encora.musicplayer.di.module

import com.encora.musicplayer.data.local.dao.SongsDao
import com.encora.musicplayer.data.remote.api.MusicPlayerService
import com.encora.musicplayer.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SongRepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        musicPlayerService:  MusicPlayerService,
        songsDao: SongsDao
    ): MainRepository {
        return MainRepository(musicPlayerService, songsDao)
    }



}
