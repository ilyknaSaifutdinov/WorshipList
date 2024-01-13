package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.room.SongDB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

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

        songsAdapter = SongsAdapter(30)
        songsList.adapter = songsAdapter

        init()

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

    private fun getSong(){
        val db = SongDB.getDB(this)
        var doc : Document
        db.getDao()
        doc = Jsoup.connect("https://holychords.pro/57").get()
        val textSong : Elements = doc.getElementsByTag("pre")
        Log.d("MyTag", textSong.get(1).text())
    }
}