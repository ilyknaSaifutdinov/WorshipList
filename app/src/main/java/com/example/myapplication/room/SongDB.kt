package com.example.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [SongEntity::class], version = 1)
abstract class SongDB : RoomDatabase() {

    abstract fun getDao() : Dao

    companion object{
        fun getDB(context: Context): SongDB {
            return Room.databaseBuilder(
                context.applicationContext,
                SongDB::class.java,
                "song.db"
            ).build()
        }
    }
}