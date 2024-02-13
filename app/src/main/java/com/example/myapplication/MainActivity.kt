package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Song
import com.example.myapplication.data.SongDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

const val URL = "https://holychords.pro/"
const val TAG_TEXT_SONG = "pre"
const val TAG_NAME_SONG = "h2"
const val CLASS_AUTHOR_SONG = "text-white d-block d-sm-inline"

val songIds = listOf(57, 16936, 10211, 1798, 39684, 5898, 3, 2606, 2641, 37, 3297,
    5967, 938, 43704)

class MainActivity : AppCompatActivity() {
    private lateinit var songAdapter: SongsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songsList: RecyclerView = findViewById(R.id.songList)

        songAdapter = SongsAdapter()
        songsList.adapter = songAdapter

        requestSite()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getSong() = GlobalScope.launch(Dispatchers.IO) {
        val db = SongDB.createDataBase(applicationContext)

        songIds.forEach { id ->
            try {
                val doc = Jsoup.connect("$URL$id").get()
                val textSong = doc.getElementsByTag(TAG_TEXT_SONG)[1].text()
                val textName = doc.getElementsByTag(TAG_NAME_SONG)[0].text()
                val textAuthor = doc
                    .getElementsByClass(CLASS_AUTHOR_SONG)[0].text()
                
                db.getDao().insertSongs(Song(null, textName, textAuthor, textSong))
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка при получении данных песни.", e)
            }
        }

        // Load the songs from the database on a background thread
        val songs = withContext(Dispatchers.IO) {
            db.getDao().getAllSongs()
        }

        // Update the adapter on the main thread
        withContext(Dispatchers.Main) {
            songAdapter.updateSongs(songs)
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun requestSite() {
        GlobalScope.launch(Dispatchers.Main) {
            getSong()
        }
    }
}