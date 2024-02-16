package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Song

class SongsAdapter(
    private val onItemClickListener: OnItemClickListener,
    private var itemsSong: List<Song>
) : RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {

    companion object {
        const val EXTRA_SONG = "song"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongsViewHolder(itemView, itemsSong, onItemClickListener)
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

    class SongsViewHolder(
        itemView: View,
        private val itemsSong: List<Song>,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var songsIDTV: TextView = itemView.findViewById(R.id.songsIDTV)
        private var songsNameTV: TextView = itemView.findViewById(R.id.songsNameTV)
        private var authorTV: TextView = itemView.findViewById(R.id.author_tv)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(song: Song) {
            songsIDTV.text = song.id.toString()
            songsNameTV.text = song.songName
            authorTV.text = song.songAuthor
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(itemsSong[adapterPosition])
        }
    }

    interface OnItemClickListener { fun onItemClick(item: Song) }
}