package net.diogoparreira.musicapp.musicrepository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteMusicAPI {
    @GET("?method=geo.gettoptracks&format=json")
    fun getMusicList(@Query("country") countryName: String, @Query("api_key") apiKey: String): Call<Tracks>
}