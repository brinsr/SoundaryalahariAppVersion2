package com.example.soundaryalahariappversion2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundaryalahariappversion2.data.OfflineShlokaRepository
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.data.ShlokaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * ViewModel to retrieve all items in the Room database
 */
class HomeViewModel (
            shlokaRepository: ShlokaRepository,
    ) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        shlokaRepository.getAllShlokasStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for HomeScreen
 *
 */
data class HomeUiState(
        val shlokaList: List<Shloka> = listOf()
)

data class SearchUiState(
    var searchNum: String? = ""
)