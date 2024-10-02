package com.example.livedataandflow

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.databinding.ActivityMainBinding
import com.example.livedataandflow.flow.FlowActivity
import com.example.livedataandflow.image.BlurActivity
import com.example.livedataandflow.lottie.LottieActivity
import com.example.livedataandflow.system.SystemActivity
import com.example.livedataandflow.widget.WidgetActivity
import com.example.livedataandflow.workmanager.WMActivity

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            flow.setOnClickListener {
                Intent(applicationContext, FlowActivity::class.java).run {
                    startActivity(this)
                }
            }

            WorkManager.setOnClickListener {
                Intent(applicationContext, WMActivity::class.java).run {
                    startActivity(this)
                }
            }
            blur.setOnClickListener {
                Intent(applicationContext, BlurActivity::class.java).run {
                    startActivity(this)
                }
            }
            widget.setOnClickListener {
                Intent(applicationContext, WidgetActivity::class.java).run {
                    startActivity(this)
                }
            }
            system.setOnClickListener {
                Intent(applicationContext, SystemActivity::class.java).run {
                    startActivity(this)
                }
            }
            lottie.setOnClickListener {
                Intent(applicationContext, LottieActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        Log.d(TAG, "onCreate")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
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
}