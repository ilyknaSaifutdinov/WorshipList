package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.jsoup.select.Elements

@Dao
interface Dao {
    @Insert
    fun insertItem(songEntity: SongEntity)
    @Query("SELECT * FROM SONGS")
    fun getAllSongs(): Flow<List<SongEntity>>
}