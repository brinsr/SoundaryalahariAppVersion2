package com.example.soundaryalahariappversion2.data

import kotlinx.coroutines.flow.Flow

class OfflineShlokaRepository(private val shlokaDao: ShlokaDao): ShlokaRepository {
 override fun getAllShlokasStream(): Flow<List<Shloka>> =
    shlokaDao.getAllShlokas()

override fun getShlokaStream(number: Int): Flow<Shloka?> =
    shlokaDao.getShloka(number)

override suspend fun insertShloka(shloka: Shloka)  =
    shlokaDao.insert(shloka)

override suspend fun deleteShloka(shloka: Shloka) =
    shlokaDao.delete(shloka)

override suspend fun updateShloka(shloka: Shloka)  =
    shlokaDao.update(shloka)
}