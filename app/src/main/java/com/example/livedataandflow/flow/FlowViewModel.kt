package com.example.livedataandflow.flow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class FlowViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val _task = MutableLiveData("none")
    val task: LiveData<String>
        get() = _task

    private val _work = MutableStateFlow("none")
    val work = _work.asStateFlow()

    private val _pollingPlayerState = Channel<Unit>()
    val pollingPlayerState = _pollingPlayerState.receiveAsFlow()
//    private val _pollingPlayerState = MutableSharedFlow<Unit>()
//    val pollingPlayerState = _pollingPlayerState.asSharedFlow()

    fun doSetWork() {
        _work.tryEmit("바뀌냐??")
    }

    fun triggerPollingPlayerState() {
        Log.d(TAG, "in triggerPollingPlayerState")
        val result = _pollingPlayerState.trySend(Unit)
//        viewModelScope.launch {
//            Log.d(TAG, "in _pollingPlayerState.emit(Unit)")
//            _pollingPlayerState.emit(Unit)
//        }

        Log.d(TAG, "result.isSuccess: ${result.isSuccess}")
        Log.d(TAG, "result.isFailure: ${result.isFailure}")
        Log.d(TAG, "result.isClosed: ${result.isClosed}")
    }
}