package net.diogoparreira.musicapp

import android.app.Application
import net.diogoparreira.musicapp.musicrepository.RemoteMusicRepository
import net.diogoparreira.musicapp.musicrepository.Tracks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MusicListCallback {
    fun onReceiveList(musicList: List<MusicEntryData>)
}

class MusicApplication : Application() {
    companion object {
        private lateinit var inst: MusicApplication
        val instance: MusicApplication
            get() = inst
    }

    lateinit var remoteMusicRepository: RemoteMusicRepository

    override fun onCreate() {
        super.onCreate()
        inst = this
        remoteMusicRepository = RemoteMusicRepository()
    }

    fun getMusicList(country: String, callback: MusicListCallback) {

        val musicList : Call<Tracks> = remoteMusicRepository.getMusicList(country)

        musicList.enqueue(object : Callback<Tracks> {
            override fun onFailure(call: Call<Tracks>?, t: Throwable?) {
                callback.onReceiveList(emptyList<MusicEntryData>())
            }

            override fun onResponse(call: Call<Tracks>?, response: Response<Tracks>?) {
                val body = response!!.body()
                callback.onReceiveList(body!!.tracks.track)
            }
        })
    }

}