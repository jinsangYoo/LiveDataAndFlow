package com.example.livedataandflow.widget

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.R
import com.example.livedataandflow.widget.ExampleAppWidgetProvider.Companion.ACTION_UPDATE_COUNT
import com.example.livedataandflow.widget.ExampleAppWidgetProvider.Companion.EXTRA_COUNT
import com.example.livedataandflow.databinding.ActivityWidgetBinding
import kotlin.random.Random

class WidgetActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityWidgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWidgetBinding.inflate(layoutInflater).apply {
            count.setOnClickListener {
                sendBroadcast(Intent(ACTION_UPDATE_COUNT).apply {
                    component = ComponentName(this@WidgetActivity, ExampleAppWidgetProvider::class.java)
                    putExtra(EXTRA_COUNT, Random.nextInt(100))
                })
            }
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}