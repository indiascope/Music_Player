package com.encora.musicplayer.data.local.dao

import androidx.room.*
import com.encora.musicplayer.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for [com.encora.musicplayer.data.local.MusicPlayerDatabase]
 */
@Dao
interface SongsDao {

    /**
     * Inserts [songs] into the [SongEntity.TABLE_NAME] table.
     * Duplicate values are replaced in the table.
     * @param songs
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateSongs(songs: List<SongEntity>)

    /**
     * Deletes all the songs from the [SongEntity.TABLE_NAME] table.
     */
    @Query("DELETE FROM ${SongEntity.TABLE_NAME}")
    suspend fun deleteAllSongs()


    @Transaction
    suspend fun deleteAndInsertSongs(songs: List<SongEntity>) {
        deleteAllSongs()
        insertUpdateSongs(songs)
    }

    /**
     * Fetches the  song rom the [SongEntity.TABLE_NAME] table whose id is [songId].
     * @param songId Unique ID of [SongEntity]
     * @return [Flow] of [SongEntity] from database table.
     */
    @Query("SELECT * FROM ${SongEntity.TABLE_NAME} WHERE ID = :songId")
    fun getSongById(songId: Int): Flow<SongEntity>

    /**
     * Fetches all the songs from the [SongEntity.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${SongEntity.TABLE_NAME}")
    suspend fun getAllSongs(): List<SongEntity>

    /**
     * Fetches all the songs from the [SongEntity.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${SongEntity.TABLE_NAME}")
    fun getAllSongsFlow(): Flow<List<SongEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(entry: SongEntity)


    @Query("SELECT COUNT(*) from ${SongEntity.TABLE_NAME}")
    suspend fun songCount(): Int
}
