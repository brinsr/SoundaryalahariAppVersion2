package com.example.soundaryalahariappversion2.data

import kotlinx.coroutines.flow.Flow

interface ShlokaRepository {/**
 * Retrieve all the items from the given data source
 */
fun getAllShlokasStream(): Flow<List<Shloka>>
    /**
     * Retrieve an item from the given data source that matches with the [id]
     */
    fun getShlokaStream(number: Int): Flow<Shloka?>
    /**
     * Insert item in the data source
     */
    suspend fun insertShloka(shloka: Shloka)
    /**
     * Delete item in the data source
     */
    suspend fun deleteShloka(shloka: Shloka)
    /**
     * Update item in the data source
     */
    suspend fun updateShloka(shloka: Shloka)
}


