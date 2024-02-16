package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Song::class], version = 1)
abstract class SongDB : RoomDatabase() {

    abstract fun getDao() : Dao

    companion object{
        fun createDataBase(context: Context): SongDB {
            return Room.databaseBuilder(
                context,
                SongDB::class.java,
                "songs.db"
            ).build()
        }
    }
}