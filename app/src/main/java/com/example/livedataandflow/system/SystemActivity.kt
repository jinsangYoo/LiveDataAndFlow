package com.example.livedataandflow.system

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.R
import com.example.livedataandflow.databinding.ActivitySystemBinding
import com.example.livedataandflow.string.defaultEmpty
import java.util.Locale


class SystemActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val viewModel: SystemViewModel by viewModels()
    private lateinit var binding: ActivitySystemBinding

    private val SPAINISH = Locale.forLanguageTag("es").language // "es"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySystemBinding.inflate(layoutInflater).apply {
            btnLang.setOnClickListener {
                Log.d(TAG, "Locale.getDefault(): ${Locale.getDefault().language}")
                txtSystemLanguage.text = Locale.getDefault().language
            }
            btnApplySystemLang.setOnClickListener {
                txtSystemLanguage.text = Locale.getDefault().language
            }
            btnApplyEnLang.setOnClickListener {
                updateLocale(applicationContext, Locale.ENGLISH.language)
                txtSystemLanguage.text = Locale.ENGLISH.language
            }
            btnApplyKoLang.setOnClickListener {
                updateLocale(applicationContext, Locale.KOREAN.language)
                txtSystemLanguage.text = Locale.KOREAN.language
            }
            btnApplyEsLang.setOnClickListener {
                updateLocale(applicationContext, SPAINISH)
                txtSystemLanguage.text = SPAINISH
            }
            txtSystemLanguage.text = getLocaleDefaultLanguageTag()

            btnKeepScreenOnTurnOn.setOnClickListener {
                txtKeepScreenOn.text = resources.getText(R.string.turn_on)
                changeScreenOnState(true)
            }
            btnKeepScreenOnTurnOff.setOnClickListener {
                txtKeepScreenOn.text = resources.getText(R.string.turn_off)
                changeScreenOnState(false)
            }
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate")
    }

    private fun updateLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Log.d(TAG, "will apply language: $language, locale.language: ${locale.language}")
        Locale.setDefault(locale)

        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        val appLocale = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

        Log.d(TAG, "Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU: ${Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU}")
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//            AppCompatDelegate.setApplicationLocales(appLocale)
//        }

        return context.createConfigurationContext(configuration)
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
        Log.d(TAG, "in onResume::AppCompatDelegate.getApplicationLocales(): ${AppCompatDelegate.getApplicationLocales().toLanguageTags().defaultEmpty("-")}")
        Log.d(TAG, "in onResume::getLocaleDefaultLanguageTag(): ${getLocaleDefaultLanguageTag()}")

        Log.d(TAG, "in onResume::Locale.getDefault(): ${Locale.getDefault().language}")
    }

    private fun getLocaleDefaultLanguageTag(): String {
        return AppCompatDelegate.getApplicationLocales().toLanguageTags().defaultEmpty(Locale.getDefault().language)
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

    private fun changeScreenOnState(onOff: Boolean) {
        if (onOff) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}