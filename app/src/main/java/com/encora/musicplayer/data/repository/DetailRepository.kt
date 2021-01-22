package com.encora.musicplayer.data.repository

import com.encora.musicplayer.data.local.dao.SongsDao
import javax.inject.Inject

class DetailRepository @Inject constructor(
  private val songsDao: SongsDao
) : Repository {

  fun getSongsById(id: Int) = songsDao.getSongById(id)
}
