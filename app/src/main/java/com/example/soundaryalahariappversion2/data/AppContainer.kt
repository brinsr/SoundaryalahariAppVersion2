package com.example.soundaryalahariappversion2.data
import android.content.Context

/**
 * App container for Dependency injection
 */
interface AppContainer {
    val shlokaRepository:ShlokaRepository
}

class AppDataContainer(private val context: Context ): AppContainer {
    //Implementation for [ShlokaRepository
    override val shlokaRepository: ShlokaRepository by lazy {
        OfflineShlokaRepository(SoundaryalahariDatabase.getDatabase(context).shlokaDao())
    }
}
