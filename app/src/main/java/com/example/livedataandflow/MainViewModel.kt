package com.example.livedataandflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
) : ViewModel() {

    init {
        viewModelScope.launch {
        }
    }
}