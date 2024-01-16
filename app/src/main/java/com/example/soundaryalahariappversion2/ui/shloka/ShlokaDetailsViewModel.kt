package com.example.soundaryalahariappversion2.ui.shloka

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.data.ShlokaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve , update and delete an item from the [ShlokaRepository]'s data source
 */

class ShlokaDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val shlokaRepository: ShlokaRepository
) : ViewModel() {
    //this is the number of the shloka that was clicked
    private  val shlokaNumber: Int = checkNotNull(savedStateHandle[ShlokaDetailsDestination.shlokaNumberArg])

    val shlokaDetailUiState: StateFlow<ShlokaDetailsUiState> =
        shlokaRepository.getShlokaStream(shlokaNumber)
            .filterNotNull()
            .map{
                ShlokaDetailsUiState(shlokaNum = it.number, shloka = it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ShlokaDetailsUiState(1,Shloka(0,1,"", "","",""))
            )
    companion object{
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteShloka(){
        shlokaRepository.deleteShloka(shlokaDetailUiState.value.shloka)
    }
}

data class ShlokaDetailsUiState(
    val shlokaNum:Int,
    val shloka:Shloka,
    //added this in AttemptThreeAudioSliderSyncWithMediaPlayer
    )

