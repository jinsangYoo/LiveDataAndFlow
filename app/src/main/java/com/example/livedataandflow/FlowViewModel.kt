package com.example.livedataandflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlowViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val _task = MutableLiveData("none")
    val task: LiveData<String>
        get() = _task

    private val _work = MutableStateFlow("none")
    val work = _work.asStateFlow()

    fun doSetWork() {
        _work.tryEmit("바뀌냐??")
    }
}