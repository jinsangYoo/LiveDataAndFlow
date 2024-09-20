package com.example.livedataandflow.system

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.R
import com.example.livedataandflow.databinding.ActivitySystemBinding
import java.util.Locale


class SystemActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val viewModel: SystemViewModel by viewModels()
    private lateinit var binding: ActivitySystemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySystemBinding.inflate(layoutInflater).apply {
            // en, ko, es
            txtSystemLanguage.text = Locale.getDefault().language
            btnLang.setOnClickListener {
                txtSystemLanguage.text = Locale.getDefault().language
            }
            btnApplySystemLang.setOnClickListener {
                Locale.setDefault(Locale.getDefault())
                txtSystemLanguage.text = Locale.getDefault().language
            }
            btnApplyEngLang.setOnClickListener {
                txtSystemLanguage.text = Locale.ENGLISH.language
                updateResources(applicationContext, Locale.ENGLISH.language)
            }
            btnApplyKorLang.setOnClickListener {
                txtSystemLanguage.text = Locale.KOREAN.language
                updateResources(applicationContext, Locale.KOREAN.language)
            }
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            val android14OrAboveLocale = LocaleListCompat.forLanguageTags(language)
            AppCompatDelegate.setApplicationLocales(android14OrAboveLocale)
        }
        return context.createConfigurationContext(configuration)
    }
}