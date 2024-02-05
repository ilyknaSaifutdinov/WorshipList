package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.room.Song
import com.example.myapplication.room.SongDB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

const val URL = "https://holychords.pro/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songsList: RecyclerView
        val songsAdapter: SongsAdapter

        songsList = findViewById(R.id.songList)

        val layoutManager = LinearLayoutManager(this)
        songsList.layoutManager = layoutManager

        songsList.setHasFixedSize(true)

        songsAdapter = SongsAdapter()
        songsList.adapter = songsAdapter

        init()

    }

    private fun getSong(){
        val db = SongDB.getDB(this)

        var doc : Document
        val i = arrayOf(57, 16936, 10211, 1798, 39684).toMutableList()

        for (item in i) {
            i.forEach {
                doc = Jsoup.connect(URL + it).get()
                val textSong : Elements = doc.getElementsByTag("pre")
                val textName : Elements = doc.getElementsByTag("h2")
                val textAuthor : Elements = doc
                    .getElementsByClass("text-white d-block d-sm-inline")
                db.getDao().insertItem(Song(null, textName[0].text(),
                    textAuthor[0].text(), textSong[1].text()))
            }
        }
    }

    private fun init(){
        val secTread : Thread
        val runnable : Runnable = Runnable {
            run {
                getSong()
            }
        }
        secTread = Thread(runnable)
        secTread.start()
    }
}