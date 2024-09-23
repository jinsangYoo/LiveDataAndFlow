package com.example.livedataandflow.flow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    private val flow = flow<Int> {
        for (i in 1..10) {
            emit(i)
            delay(100)
        }
    }

    fun triggerCollect() {
        viewModelScope.launch {
            flow.collect {
                delay(1000)
                Log.d(TAG, "got $it")
            }
        }
    }

    fun triggerCollectLatest() {
        viewModelScope.launch {
            flow.collectLatest {
                delay(1000)
                Log.d(TAG, "got $it")
            }
        }
    }

    private val flowForConflate = flow<Int> {
        for (i in 1..10) {
            emit(i)
            delay(3000)
        }
    }

    fun triggerConflateCollect() {
        viewModelScope.launch {
            flowForConflate.onEach {
                Log.d(TAG, "number >> emit $it")
            }
            .conflate().collect {
                delay(5000)
                Log.d(TAG, "number >> consume $it")
            }
        }
    }

    private val flowForHotStream = flow<Int> {
        for (i in 1..10) {
            delay(1000)
            emit(i)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = 99
    )

    fun triggerCollectHotStream() {
        viewModelScope.launch {
//            delay(5000)
            flowForHotStream.collect {
                Log.d(TAG, "1. number >> consume $it")
            }
        }
        viewModelScope.launch {
            delay(5000)
            flowForHotStream.collect {
                Log.d(TAG, "2. number >> consume $it")
            }
        }
    }

    private val _count = MutableStateFlow<Int>(0)
    val count = _count.asStateFlow()

    fun addCount() {
        viewModelScope.launch {
            _count.value++
        }
    }
}