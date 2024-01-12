package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent

class SongsAdapter(val numberOfItems : Int) :
    RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {
    val numberItems = numberOfItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return numberItems
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val id = position + 1
        holder.bind(id)
    }

    class SongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clickSong = OnClickListener {

        }

        val songsIDTV : TextView = itemView.findViewById(R.id.songsIDTV)
        val songsNameTV : TextView = itemView.findViewById(R.id.songsNameTV)
        val authorTV : TextView = itemView.findViewById(R.id.author_tv)

        fun bind(songID : Int) {
            songsIDTV.setText(songID.toString())
            songsNameTV.setText("Славь душа, Господа")
            authorTV.setText("Слово Жизни Music")
        }
    }
}