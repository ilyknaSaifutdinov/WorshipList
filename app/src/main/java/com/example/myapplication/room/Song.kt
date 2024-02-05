package com.example.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    @ColumnInfo(name = "name")
    var songName : String,
    @ColumnInfo(name = "author")
    var songAuthor : String,
    @ColumnInfo(name = "text")
    var songText : String
)
