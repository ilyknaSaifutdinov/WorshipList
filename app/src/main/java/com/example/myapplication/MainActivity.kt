package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        init()

        val songsList: RecyclerView
        val songsAdapter: SongsAdapter

        songsList = findViewById(R.id.songList)

        val layoutManager = LinearLayoutManager(this)
        songsList.layoutManager = layoutManager

        songsList.setHasFixedSize(true)

        songsAdapter = SongsAdapter(30)
        songsList.adapter = songsAdapter

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
        var i = 1
        var doc : Document
        val db = SongDB.getDB(this)
        db.getDao()
        while (i < 5){
            doc = Jsoup.connect("https://holychords.pro/$i").get()
            val textSong : Elements = doc.getElementsByTag("pre")
            textSong.get(1).text()
            db.getDao().insertItem(textSong)
            i++
        }
    }
}