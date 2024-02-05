package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.room.Song
import com.example.myapplication.room.SongDB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class SongsAdapter() :
    RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {

        private var items = ArrayList<Song>()
        fun setListData(data: ArrayList<Song>) {
            this.items = data
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class SongsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val songsIDTV : TextView = itemView.findViewById(R.id.songsIDTV)
        val songsNameTV : TextView = itemView.findViewById(R.id.songsNameTV)
        val authorTV : TextView = itemView.findViewById(R.id.author_tv)

        fun bind(data: Song) {
            songsIDTV.text = data.id.toString()
            songsNameTV.text = data.songName
            authorTV.text = data.songAuthor
        }
    }
}