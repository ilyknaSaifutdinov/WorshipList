package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertItem(song: Song)
    @Query("SELECT * FROM SONGS")
    fun getAllSongs(): Flow<List<Song>>
}