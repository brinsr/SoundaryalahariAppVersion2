package com.example.soundaryalahariappversion2

import android.app.Application
import com.example.soundaryalahariappversion2.data.AppContainer
import com.example.soundaryalahariappversion2.data.AppDataContainer

class SoundaryalahariApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer


    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
