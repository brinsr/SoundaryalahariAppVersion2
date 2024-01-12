package com.example.soundaryalahariappversion2.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    var searchNum by mutableStateOf("")
        private set

    fun updateSearchNum(newSearchNum:String) {
        searchNum = newSearchNum
    }
}