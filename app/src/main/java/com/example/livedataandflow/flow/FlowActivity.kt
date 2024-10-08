package com.example.livedataandflow.flow

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.livedataandflow.R
import com.example.livedataandflow.databinding.ActivityFlowBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FlowActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityFlowBinding
    private val viewModel: FlowViewModel by viewModels()
    private val REPEAT_INTERVAL = 2.seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFlowBinding.inflate(layoutInflater).apply {
            addCountAtStateFlow.setOnClickListener {
                viewModel.addCount()
            }

            makeFlow.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        val repeatableFlow = makeFlow()
                        Log.d(TAG, "first collection")
                        repeatableFlow.collect { value ->
                            Log.d(TAG, "makeFlow::got $value")
                        }
                        Log.d(TAG, "collecting again")
                        repeatableFlow.collect { value ->
                            Log.d(TAG, "makeFlow::got $value")
                        }
                        Log.d(TAG, "second collection completed")
                    }
                }
            }

            makeFlowWithEmit.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        val repeatableFlow = makeFlowWithEmit()
                        Log.d(TAG, "first collection")
                        repeatableFlow.collect { value ->
                            Log.d(TAG, "makeFlowWithEmit::got $value")
                        }
                        Log.d(TAG, "collecting again")
                        repeatableFlow.collect { value ->
                            Log.d(TAG, "makeFlowWithEmit::got $value")
                        }
                        Log.d(TAG, "second collection completed")
                    }
                }
            }

            makeFlowWithEmitTake2.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        val repeatableFlow = makeFlowWithEmit().take(2)  // we only
                        // care about the first two elements
                        Log.d(TAG, "first collection")
                        repeatableFlow.collect { value ->
                            Log.d(TAG, "makeFlowWithEmitTake2::got $value")
                        }
                        Log.d(TAG, "collecting again")
                        repeatableFlow.collect { value ->
                            Log.d(TAG, "makeFlowWithEmitTake2::got $value")
                        }
                        Log.d(TAG, "second collection completed")
                    }
                }
            }

            manualEmit.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.doSetWork()
                    }
                }
            }

            triggerChannel.setOnClickListener {
                viewModel.triggerPollingPlayerState()
            }

            sharedflow.setOnClickListener {
                Intent(applicationContext, SharedFlowActivity::class.java).run {
                    startActivity(this)
                }
            }

            collect.setOnClickListener {
                viewModel.triggerCollect()
            }

            collectLatest.setOnClickListener {
                viewModel.triggerCollectLatest()
            }

            conflateCollect.setOnClickListener {
                viewModel.triggerConflateCollect()
            }

            collectHotStream.setOnClickListener {
                viewModel.triggerCollectHotStream()
            }

            channel.setOnClickListener {
                Intent(applicationContext, ChannelActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.flow)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        observe()
        Log.d(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
//        viewModel.triggerPollingPlayerState()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
//        viewModel.triggerPollingPlayerState()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged")
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pollingPlayerState.collectLatest {
                        Log.d(TAG, "in viewModel.pollingPlayerState.collectLatest")
                        pollingPlayerState()
                            .collectLatest {
                                Log.d(TAG, "pollingPlayerState::got $it")
                            }
                    }
                }

                launch {
                    viewModel.work.collectLatest {
                        binding.result.text = it
                    }
                }

                launch {
                    viewModel.count.collectLatest {
                        Log.d(TAG, "count: $it")
                    }
                }
            }
        }
    }

    private fun pollingPlayerState() = flow {
        while (true) {
            emit(Unit)
            kotlinx.coroutines.delay(REPEAT_INTERVAL)
        }
    }

    private fun makeFlow() = flow {
        Log.d(TAG, "makeFlow...1")
        Log.d(TAG, "makeFlow...2")
        Log.d(TAG, "makeFlow...3")
        Log.d(TAG, "done")
        emit(false)
    }

    private fun makeFlowWithEmit() = flow {
        Log.d(TAG, "sending first value")
        emit(1)
        Log.d(TAG, "first value collected, sending another value")
        emit(2)
        Log.d(TAG, "second value collected, sending a third value")
        emit(3)
        Log.d(TAG, "done")
    }
}