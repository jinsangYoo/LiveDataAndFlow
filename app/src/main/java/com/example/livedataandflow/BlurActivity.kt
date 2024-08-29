package com.example.livedataandflow

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.databinding.ActivityBlurBinding
import com.example.livedataandflow.databinding.ActivityMainBinding

class BlurActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlurBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBlurBinding.inflate(layoutInflater).apply {
            renderEffect.setOnClickListener {
                val bmp = Bitmap.createBitmap(miniplayer.width, miniplayer.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bmp)
//                val color = Color.parseColor("#66ffffff") //40%
                val color = Color.parseColor("#ccffffff") //80%
//                val color = Color.parseColor("#66FF3D33")
                canvas.drawColor(color)

                miniplayer.setImageBitmap(bmp)

                val blurRenderEffect = RenderEffect.createBlurEffect(
                    25.0f,
                    25.0f,
                    Shader.TileMode.MIRROR
                )
                miniplayer.setRenderEffect(blurRenderEffect)
            }

            blur.setOnClickListener {
                val bmp = Bitmap.createBitmap(miniplayer.width, miniplayer.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bmp)
//                val color = Color.parseColor("#66ffffff") //40%
                val color = Color.parseColor("#ccffffff") //80%
//                val color = Color.parseColor("#66FF3D33")
                canvas.drawColor(color)

//                val blurredBitmap = Toolkit.blur(bmp, 25.0f)
//                miniplayer.setImageBitmap(blurredBitmap)
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