package com.encora.musicplayer.data.repository

import androidx.annotation.WorkerThread
import com.encora.musicplayer.data.local.dao.SongsDao
import com.encora.musicplayer.data.local.entity.SongEntity
import com.encora.musicplayer.data.local.entity.mapDto
import com.encora.musicplayer.data.remote.api.MusicPlayerService
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class MainRepository @Inject constructor(
  private val musicPlayerService: MusicPlayerService,
  private val songsDao: SongsDao
) : Repository {

    init {
        Timber.d("Injection MainRepository")
    }

    @WorkerThread
    suspend fun loadSongs(fetchLatestSong:Boolean=false,
        onSuccess: (List<SongEntity>) -> Unit,
        onError: (String) -> Unit) {
        val songs: List<SongEntity> = songsDao.getAllSongs()
        if (songs.isEmpty() || fetchLatestSong) {
            try {
                // request API network call asynchronously.
                val songListResponse  = musicPlayerService.getTopSongs()
                if (songListResponse.songEntryList != null && songListResponse.songEntryList.isNotEmpty()) {
                    songListResponse.songEntryList.map { it.mapDto() }.let {
                        songsDao.insertUpdateSongs(it)
                        onSuccess(it)
                    }
                    Timber.d("we got response")
                } else {
                    onSuccess(emptyList())
                }
            } catch (exception: Exception) {
                if(exception is UnknownHostException || exception is TimeoutException ||
                        exception is IOException){
                    onError("No Internet Connection")
                }else{
                    onError(exception.localizedMessage?:"Error occurred in Fetching Data")
                }

            }
        } else {
            onSuccess(songs)
        }
    }
}
