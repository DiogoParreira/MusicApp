package net.diogoparreira.musicapp

import android.app.Application

class MusicApplication : Application() {
    companion object {
        private lateinit var inst: MusicApplication
        val instance: MusicApplication
            get() = inst
    }

    override fun onCreate() {
        super.onCreate()
        inst = this
    }

}