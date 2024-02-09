package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(song: Song)
    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<Song>>
}