package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.example.myapplication.SongsAdapter.Companion.EXTRA_SONG
import com.example.myapplication.data.Song
import com.google.android.material.appbar.MaterialToolbar

class SongActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        val nameSongMTB: MaterialToolbar = findViewById(R.id.toolAppBar)
        val textSongTV: TextView = findViewById(R.id.textSong)

        val song = intent.getSerializableExtra(EXTRA_SONG) as Song
        nameSongMTB.title = song.songName
        nameSongMTB.subtitle = song.songAuthor
        textSongTV.text = song.songText
    }

    fun addToFavorites(item: MenuItem) {}
}