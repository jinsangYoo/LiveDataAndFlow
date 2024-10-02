package com.example.livedataandflow.locale

import androidx.appcompat.app.AppCompatDelegate
import com.example.livedataandflow.string.defaultEmpty
import java.util.Locale

object LocaleUtil {
    fun getLocaleDefaultLanguageTag(): String {
        return AppCompatDelegate.getApplicationLocales().toLanguageTags().defaultEmpty(Locale.getDefault().language)
    }
}