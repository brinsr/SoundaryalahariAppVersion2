package com.example.soundaryalahariappversion2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Shloka::class],version = 5)
abstract class SoundaryalahariDatabase :RoomDatabase() {
    abstract fun shlokaDao():ShlokaDao
    companion object {
        @Volatile
        private var Instance: SoundaryalahariDatabase? = null
        //check inventory app for the detailed explanation for changes,
        //I needed to be able to save the table
        fun getDatabase(context: Context): SoundaryalahariDatabase {
//                return Instance ?: synchronized(this){
//                    Room.databaseBuilder(context = context ,SoundaryalahariDatabase::class.java,"shloka_database")
//                        .createFromAsset("database/shloka_database.db") // I saved this from device file explorer
//                        .fallbackToDestructiveMigration()
//                        .build()
//                        .also { Instance = it }
//                }
            val db = Instance ?: synchronized(this){
                Room.databaseBuilder(context = context ,SoundaryalahariDatabase::class.java,"shloka_database")
                    .createFromAsset("database/shloka_database.db") // I saved this from device file explorer

                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
            //stack overflow solution to synchronize
            val cursor = db.query("PRAGMA wal_checkpoint", arrayOf())
            cursor.moveToFirst()
            return db
        }
    }
}