package com.example.livedataandflow.flow

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
import com.example.livedataandflow.databinding.ActivityChannelBinding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ChannelActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityChannelBinding
    private val viewModel: ChannelViewModel by viewModels()
    private val channel = Channel<Int>()
    private val MAX_LOOP = 5
    val flowFromChannel = channel.consumeAsFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChannelBinding.inflate(layoutInflater).apply {
            sendChannel.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        for (x in 1..MAX_LOOP) channel.send(x * x)
                        Log.d(TAG, "Done for loop")
//                        channel.close()
                    }
                    Log.d(TAG, "Done sending")
                }
            }
            receiveChannel.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        repeat(MAX_LOOP) {
                            val value = channel.receive()
                            Log.d(TAG, "in receiveChannel")
                            Log.d(TAG, "receive(): $value")
                        }
                    }
                }
            }
            receiveFlowChannel.setOnClickListener {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        channel.receiveAsFlow().collectLatest {
                            Log.d(TAG, "in receiveFlowChannel")
                            Log.d(TAG, "receiveAsFlow.collectLatest $it")
                        }
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

//        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    flowFromChannel.collect{
                        Log.d(TAG, "1. flowFromChannel.collect $it")
                    }
                    flowFromChannel.collect{
                        Log.d(TAG, "2. flowFromChannel.collect $it")
                    }
                }
                launch {
                    channel.receiveAsFlow().collectLatest {
                        Log.d(TAG, "in channel")
                        Log.d(TAG, "receiveAsFlow.collectLatest $it")
                    }
                }
            }
        }
    }
}