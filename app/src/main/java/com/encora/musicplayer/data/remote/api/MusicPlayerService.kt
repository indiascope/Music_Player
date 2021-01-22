package com.encora.musicplayer.data.remote.api

import com.encora.musicplayer.domain.SongFeed
import retrofit2.http.GET

/**
 * Service to fetch Song Lists using end point [MUSIC_PLAYER_API_URL].
 */
interface MusicPlayerService {

    @GET("/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=20/xml")
    suspend fun getTopSongs(): SongFeed

    companion object {
        const val MUSIC_PLAYER_API_URL = "http://ax.itunes.apple.com"
    }
}
