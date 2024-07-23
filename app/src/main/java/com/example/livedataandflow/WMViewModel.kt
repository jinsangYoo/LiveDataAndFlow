package com.example.livedataandflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WMViewModel : ViewModel() {

    init {
        viewModelScope.launch {
        }
    }
}