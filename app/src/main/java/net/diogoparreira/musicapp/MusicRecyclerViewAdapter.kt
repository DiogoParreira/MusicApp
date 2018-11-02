package net.diogoparreira.musicapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class MusicRecyclerViewAdapter(var musicList: List<MusicEntryData>) : RecyclerView.Adapter<MusicRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var artist: TextView
        var artistImage: ImageView

        init {
            name = view.findViewById(R.id.name)
            artist = view.findViewById(R.id.artist)
            artistImage = view.findViewById(R.id.artistImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val music = musicList[position]

        holder.name.setText(music.name)
        holder.artist.text = MusicApplication.instance.getString(R.string.artist, music.artist.name)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}