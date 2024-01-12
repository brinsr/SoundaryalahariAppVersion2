package com.example.soundaryalahariappversion2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShlokaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shloka: Shloka)

    @Update
    suspend fun update(shloka: Shloka)

    @Delete
    suspend fun delete(shloka: Shloka)

    //Detail Screen will show chant audio, sing audio and meaning of selected shloka
    @Query(
        "SELECT * from shlokas WHERE number = :number"
//       "SELECT number,chant,sing,meaning from shlokas WHERE number = :number"
    )
    fun getShloka(number:Int): Flow<Shloka>

    //Home Screen will show list of all shlokas -number and the shloka
    @Query(
        "SELECT *  from shlokas ORDER by number ASC"
//       "SELECT number,content  from shlokas ORDER by number ASC"
    )
    fun getAllShlokas(): Flow<List<Shloka>>
}