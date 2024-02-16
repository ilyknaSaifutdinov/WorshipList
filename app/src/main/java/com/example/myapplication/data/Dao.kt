package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongs(songs: List<Song>)
    @Query("SELECT * FROM song")
    fun getAllSongs(): List<Song>
    @Query("SELECT * FROM song WHERE name || author LIKE '%' || :search || '%'")
    suspend fun getAllSongsBySearching(search: String): List<Song>
    @Query("SELECT * FROM song WHERE name LIKE :nameSong")
    suspend fun getSongByName(nameSong: String): Song
    @Query("DELETE FROM song")
    fun deleteAllSongs()
}