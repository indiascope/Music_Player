package com.encora.musicplayer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.encora.musicplayer.data.local.entity.SongEntity
import com.encora.musicplayer.data.remote.api.MusicPlayerService
import com.encora.musicplayer.data.repository.MainRepository
import com.encora.musicplayer.domain.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [MainActivity]
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val _songsLiveData = MutableLiveData<State<List<SongEntity>>>()

    val songsLiveData: LiveData<State<List<SongEntity>>> = _songsLiveData

    fun getSongs(fetchLatestSong:Boolean=false) {
        viewModelScope.launch {
            _songsLiveData.value = State.loading()
            mainRepository.loadSongs(fetchLatestSong,onSuccess = {it->
                _songsLiveData.value= State.success(it)
            },onError = {
                _songsLiveData.value= State.error(it)
            })
        }
    }
}
