package com.example.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    @ColumnInfo(name = "name")
    val songName : String,
    @ColumnInfo(name = "author")
    val songAuthor : String,
    @ColumnInfo(name = "text")
    val songText : String
)
