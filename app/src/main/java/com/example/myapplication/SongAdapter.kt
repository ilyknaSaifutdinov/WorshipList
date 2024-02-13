package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Song
import kotlinx.coroutines.flow.Flow

class SongsAdapter() :
    RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {

    private var itemsSong = emptyList<Song>()

    class SongsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private var songsIDTV : TextView = itemView.findViewById(R.id.songsIDTV)
        private var songsNameTV : TextView = itemView.findViewById(R.id.songsNameTV)
        private var authorTV : TextView = itemView.findViewById(R.id.author_tv)
        fun bind(song: Song) {
            songsIDTV.text = song.id.toString()
            songsNameTV.text = song.songName
            authorTV.text = song.songAuthor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemsSong.size
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(itemsSong[position])
    }

    fun updateSongs(songs: List<Song>) {
        itemsSong = songs
        notifyDataSetChanged()
    }
}