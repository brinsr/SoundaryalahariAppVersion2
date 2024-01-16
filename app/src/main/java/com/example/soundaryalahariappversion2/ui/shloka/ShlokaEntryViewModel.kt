package com.example.soundaryalahariappversion2.ui.shloka

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.data.ShlokaRepository

/**
 * ViewModel to validate(maybe) and insert item in Room database
 *
 */

class ShlokaEntryViewModel (private val shlokaRepository: ShlokaRepository) : ViewModel() {
    /**
     * Holds current shloka ui state
     */
    var shlokaUiState by mutableStateOf(ShlokaUiState())
        private set


    /**
     * Updates the [shlokaUiState] with value provided in the argument
     */

    fun updateUiState(shloka: Shloka) {
        shlokaUiState =
            ShlokaUiState(shloka = shloka)
    }

    suspend fun saveShloka(){
        shlokaRepository.insertShloka(shlokaUiState.shloka)
    }

    /**
     * Represents UI state for shloka
     */
    data class ShlokaUiState(
        val shloka: Shloka = Shloka(0,1,"", "","","")
    )

}