package com.encora.musicplayer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.encora.musicplayer.data.local.dao.SongsDao
import com.encora.musicplayer.data.local.entity.SongEntity

/**
 * Abstract MusicPlayer database.
 * It provides DAO [SongsDao] by using method [getSongsDao].
 */
@Database(
    entities = [SongEntity::class],
    version = DatabaseMigrations.DB_VERSION
)
abstract class MusicPlayerDatabase : RoomDatabase() {

    /**
     * @return [SongsDao] Music Player  Songs Data Access Object.
     */
    abstract fun getSongsDao(): SongsDao

    companion object {
        const val DB_NAME = "music_player_database"

        @Volatile
        private var INSTANCE: MusicPlayerDatabase? = null

        fun getInstance(context: Context): MusicPlayerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicPlayerDatabase::class.java,
                    DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
