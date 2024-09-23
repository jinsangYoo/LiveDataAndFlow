package com.example.livedataandflow.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SharedFlowViewModel : ViewModel() {
    private val _count = MutableSharedFlow<Int>()
    val count = _count.asSharedFlow()

    init {
        viewModelScope.launch {
            _count.emit(0)
        }
    }

    fun addCount() {
        viewModelScope.launch {
            var x = 1
            repeat(20) {
                _count.emit(x++)
                delay(500)
            }
        }
    }
}