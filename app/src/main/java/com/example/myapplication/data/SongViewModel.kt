package com.example.myapplication.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class SongViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData: List<Song>
    private val repository: SongRepository

    init {
        val songDao = SongDB.createDataBase(application).getDao()
        repository = SongRepository(songDao)
        readAllData = repository.readAllData
    }
}