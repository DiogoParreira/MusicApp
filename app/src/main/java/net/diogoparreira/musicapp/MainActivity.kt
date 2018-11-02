package net.diogoparreira.musicapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MusicRecyclerViewAdapter
    private val musicList = mutableListOf<MusicEntryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        adapter = MusicRecyclerViewAdapter(musicList)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter


        MusicApplication.instance.getMusicList("Portugal", object: MusicListCallback {
            override fun onReceiveList(musicList: List<MusicEntryData>) {
                adapter.musicList = musicList
                adapter.notifyDataSetChanged()
            }

        })

    }
}
