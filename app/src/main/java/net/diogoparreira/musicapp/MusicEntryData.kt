package net.diogoparreira.musicapp

import com.squareup.moshi.Json

class ImageDescription(@field:Json(name = "#text") var text : String, var size : String)
class ArtistData(var name : String)

class MusicEntryData(var name : String, var artist : ArtistData, var image : List<ImageDescription>)