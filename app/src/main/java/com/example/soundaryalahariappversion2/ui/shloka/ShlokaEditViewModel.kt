package com.example.soundaryalahariappversion2.ui.shloka

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundaryalahariappversion2.R
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.data.ShlokaRepository
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEntryViewModel.ShlokaUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShlokaEditViewModel (
    savedStateHandle: SavedStateHandle,
    private val shlokaRepository: ShlokaRepository
) : ViewModel() {

    /**
     * Holds current shloka ui state
     * this ShlokaUiState is defined in ShlokaEntryViewModel
     * I needed to do this instead of separate viewModel for edit since the changes
     * should be reflected in one place- two separate viewmodels will not serve
     * the purpose . I think.
     *So, did it exactly like inventory codelab
     */

    var shlokaEditUiState by mutableStateOf(ShlokaUiState())
        private set

    private  val shlokaNumber:Int = checkNotNull(savedStateHandle[ShlokaEditDestination.shlokaNumArg])
//This approach immediately assigns the retrieved and transformed Shloka object
// to shlokaEditUiState. However, this method might be more suitable
// for a one-time retrieval and display, but EDITING might require additional considerations
//    init {
//        viewModelScope.launch {
//            shlokaEditUiState = shlokaRepository.getShlokaStream(shlokaNumber)
//                .filterNotNull()
//                .first()
//                .toShlokaUiState() //Extension function
//                                        // below Shloka data class in data directory
//         }
//    }

    //Not sure why, but it works only if I enter again
    init{
        viewModelScope.launch {
            shlokaEditUiState = shlokaRepository.getShlokaStream(shlokaNumber)
                .filterNotNull()
                .first()
                .toShlokaUiState()
        }
    }
    // this is what I understand- using StateFlow which is continuously observing changes and updating.

    //This approach represents the ShlokaUiState as a StateFlow and updates the UI
    // as the data changes. It's more reactive and continuously updates the UI as
    // new data arrives. This might be preferable if you need real-time updates or
    // want the UI to reflect changes immediately.
    //uistate here is like itemdetailsviewmodel
//    var shlokaEditUiState: StateFlow<ShlokaEditUiState> =
//        shlokaRepository.getShlokaStream(shlokaNumber)
//            .filterNotNull()
//            .map{
//                ShlokaEditUiState(shloka = it)
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(),
//                initialValue = ShlokaEditUiState(Shloka(0,0,"","",""))
//            )


    fun updateUiState(shloka:Shloka){
        shlokaEditUiState = ShlokaUiState(shloka)
    }

    suspend fun updateShloka(){
        shlokaRepository.updateShloka(shlokaEditUiState.shloka)
    }

    /**
     * Extension function to convert [Shloka] to [ShlokaUiState]
     * This was needed in ShlokaEditViewModel to convert to UiState
     * Separation of concerns between screens
     */


    fun Shloka.toShlokaUiState(): ShlokaUiState =
        ShlokaUiState(this)
}
