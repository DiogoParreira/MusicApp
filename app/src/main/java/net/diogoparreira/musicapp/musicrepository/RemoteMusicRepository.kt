package net.diogoparreira.musicapp.musicrepository

import android.app.Application
import net.diogoparreira.musicapp.MusicApplication
import net.diogoparreira.musicapp.MusicEntryData
import net.diogoparreira.musicapp.R
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class Track(var track: List<MusicEntryData>)
class Tracks(var tracks: Track)

class RemoteMusicRepository() {

    var service : RemoteMusicAPI
    private var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        service = retrofit.create(RemoteMusicAPI::class.java)
    }

    fun getMusicList(country: String) : Call<Tracks> {
        return service.getMusicList(country, MusicApplication.instance.getString(R.string.api_key))
    }

}