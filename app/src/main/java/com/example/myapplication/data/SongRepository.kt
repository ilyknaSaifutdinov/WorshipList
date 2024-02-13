package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

class SongRepository(private val songDao: Dao) {

    val readAllData: List<Song> = songDao.getAllSongs()

}