package com.encora.musicplayer.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.encora.musicplayer.data.repository.DetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * ViewModel for [SongDetailsActivity.kt]
 */
@ExperimentalCoroutinesApi
class SongDetailsViewModel @AssistedInject constructor(
    detailRepository: DetailRepository,
    @Assisted songId: Int
) : ViewModel() {

    val song by lazy{
        detailRepository.getSongsById(songId).asLiveData()
    }

    @AssistedFactory
    interface SongDetailsViewModelFactory {
        fun create(songId: Int): SongDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: SongDetailsViewModelFactory,
            postId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(postId) as T
            }
        }
    }
}
