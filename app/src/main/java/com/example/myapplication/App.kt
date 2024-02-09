package com.example.myapplication

import android.app.Application
import com.example.myapplication.room.SongDB

class App : Application() {
    val dataBase by lazy { SongDB.createDataBase(this) }
}