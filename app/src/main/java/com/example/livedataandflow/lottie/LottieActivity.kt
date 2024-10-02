package com.example.livedataandflow.lottie

import android.animation.Animator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.R
import com.example.livedataandflow.databinding.ActivityLottieBinding
import timber.log.Timber

class LottieActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityLottieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLottieBinding.inflate(layoutInflater).apply {
            normalLotti.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {
                    Timber.d("normal lottie start")
                }

                override fun onAnimationEnd(animation: Animator) {
                    Timber.d("normal lottie end")
                }

                override fun onAnimationCancel(animation: Animator) {
                    Timber.d("normal lottie cancel")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Timber.d("normal lottie repeat")
                }
            })
            normalPlay.setOnClickListener {
                normalLotti.setMinAndMaxFrame(4, 21)
                normalLotti.playAnimation()
            }
            normalStop.setOnClickListener {
                normalLotti.pauseAnimation()
            }
            normalCancel.setOnClickListener {
                normalLotti.setMinAndMaxFrame(1, 3)
                normalLotti.cancelAnimation()
                normalLotti.frame = 0
            }

            longLotti.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {
                    Timber.d("long lottie start")
                }

                override fun onAnimationEnd(animation: Animator) {
                    Timber.d("long lottie end")
                }

                override fun onAnimationCancel(animation: Animator) {
                    Timber.d("long lottie cancel")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Timber.d("long lottie repeat")
                }
            })
            longPlay.setOnClickListener {
                longLotti.setMinAndMaxFrame(25, 216)
                longLotti.playAnimation()
            }
            longStop.setOnClickListener {
                longLotti.pauseAnimation()
            }
            longCancel.setOnClickListener {
                longLotti.setMinAndMaxFrame(0, 246)
                longLotti.cancelAnimation()
                longLotti.frame = 0
            }
        }

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}