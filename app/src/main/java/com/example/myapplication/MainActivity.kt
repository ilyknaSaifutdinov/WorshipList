package com.example.myapplication

import android.content.Intent
import android.media.RouteListingPreference.Item
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.SongsAdapter.Companion.EXTRA_SONG
import com.example.myapplication.data.Song
import com.example.myapplication.data.SongDB
import com.example.myapplication.data.SongDB.Companion.createDataBase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.Serializable
import kotlin.properties.Delegates

const val URL = "https://holychords.pro/"
const val TAG_TEXT_SONG = "pre"
const val TAG_NAME_SONG = "h2"
const val CLASS_AUTHOR_SONG = "text-white d-block d-sm-inline"

val songIds = longArrayOf(57, 16936, 10211, 1798, 39684, 5898, 3, 2641, 37, 3297,
    5967, 938, 43704)

class MainActivity() : AppCompatActivity(), SongsAdapter.OnItemClickListener {
    private lateinit var songAdapter: SongsAdapter
    private lateinit var songsList: RecyclerView
    private var numberSong by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        songsList = findViewById(R.id.songList)

        songAdapter = SongsAdapter(this, itemsSong = emptyList())
        songsList.adapter = songAdapter

        showSongs()

        requestSite()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun showSongs() = GlobalScope.launch(Dispatchers.IO) {
        val db = createDataBase(applicationContext)
        val songs = db.getDao().getAllSongs()
        withContext(Dispatchers.Main){
            songAdapter = SongsAdapter(this@MainActivity, songs)
            songsList.adapter = songAdapter
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getSong() = GlobalScope.launch(Dispatchers.IO) {
        val db = createDataBase(applicationContext)

        val songs = mutableListOf<Song>()

        songIds.forEachIndexed { index, id ->
            try {
                val doc = Jsoup.connect("$URL$id").get()
                val textSong = doc.getElementsByTag(TAG_TEXT_SONG)[1].text()
                val textName = doc.getElementsByTag(TAG_NAME_SONG)[0].text()
                val textAuthor = doc
                    .getElementsByClass(CLASS_AUTHOR_SONG)[0].text()
                numberSong = index + 1

                val existingSong = db.getDao().getSongByName(textName)

//                if (existingSong == null) {
//                    songs.add(Song(numberSong, textName, textAuthor, textSong))
//                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Ошибка при получении данных песни.", e)
            }
        }

        // Insert the songs into the database on a background thread
        val insertedSongs = withContext(Dispatchers.IO) {
            db.getDao().insertSongs(songs)
        }

        // Load the songs from the database on a background thread
        val allSongs = withContext(Dispatchers.IO) {
            db.getDao().getAllSongs()
        }

        // Update the adapter on the main thread
        withContext(Dispatchers.Main) {
            songAdapter.updateSongs(allSongs)
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun requestSite() {
        GlobalScope.launch(Dispatchers.Main) {
            getSong()
        }
    }

    override fun onItemClick(item: Song) {
        val intent = Intent(this, SongActivity::class.java)
        intent.putExtra(EXTRA_SONG, item as Serializable)
        this.startActivity(intent)
    }
}