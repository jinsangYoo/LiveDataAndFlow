package com.example.livedataandflow

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
import com.example.livedataandflow.databinding.ActivityFlowBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class FlowActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityFlowBinding
    private val viewModel: FlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFlowBinding.inflate(layoutInflater).apply {
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.work.collectLatest {
                    binding.result.text = it
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    fun makeFlow() = flow {
        Log.d(TAG, "makeFlow...1")
        Log.d(TAG, "makeFlow...2")
        Log.d(TAG, "makeFlow...3")
        Log.d(TAG, "done")
        emit(false)
    }

    fun makeFlowWithEmit() = flow {
        Log.d(TAG, "sending first value")
        emit(1)
        Log.d(TAG, "first value collected, sending another value")
        emit(2)
        Log.d(TAG, "second value collected, sending a third value")
        emit(3)
        Log.d(TAG, "done")
    }
}