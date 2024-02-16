package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "song")
data class Song(
    @PrimaryKey var id : Int,
    @ColumnInfo(name = "name") val songName : String,
    @ColumnInfo(name = "author") val songAuthor : String,
    @ColumnInfo(name = "text") val songText : String
) : Serializable
