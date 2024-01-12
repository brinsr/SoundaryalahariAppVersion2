package com.example.soundaryalahariappversion2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shlokas")
data class Shloka(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val number: Int,
    var content:String,
    val chant: String ="",
    val sing: String = "",
    var meaning: String = ""
)
