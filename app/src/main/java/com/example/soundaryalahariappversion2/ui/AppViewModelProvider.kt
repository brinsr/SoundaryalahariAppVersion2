package com.example.soundaryalahariappversion2.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.soundaryalahariappversion2.SoundaryalahariApplication
import com.example.soundaryalahariappversion2.ui.home.HomeViewModel
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaDetailsViewModel
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEditViewModel
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEntryViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Soundaryalahari App
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        //Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                soundaryalahariApplication().container.shlokaRepository,
                )
        }
        //Initializer for ShlokaEntryViewModel
        initializer {
            ShlokaEntryViewModel(soundaryalahariApplication().container.shlokaRepository)
        }
        //Initializer for ShlokaDetailsViewModel
        initializer {
            ShlokaDetailsViewModel(
                this.createSavedStateHandle(),
                soundaryalahariApplication().container.shlokaRepository
            )
        }
        //Initializer for ShlokaEditViewModel
        initializer {
            ShlokaEditViewModel(
                this.createSavedStateHandle(),
                soundaryalahariApplication().container.shlokaRepository
            )
        }

    }
}
/**
 * Extension function to queries for [Application] object and returns an instance of
 * [SoundaryaApplication]
 */
fun CreationExtras.soundaryalahariApplication(): SoundaryalahariApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SoundaryalahariApplication)