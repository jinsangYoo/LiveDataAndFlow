package com.example.livedataandflow.workmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WMViewModel : ViewModel() {

    init {
        viewModelScope.launch {
        }
    }
}