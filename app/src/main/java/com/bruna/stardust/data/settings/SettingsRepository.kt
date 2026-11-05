package com.bruna.stardust.data.settings

import android.content.Context

class SettingsRepository(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("stardust_settings", Context.MODE_PRIVATE)

    fun load(): AppLanguage {
        val saved = prefs.getString("language", null)
        return AppLanguage.entries.find { it.name == saved } ?: AppLanguage.Portuguese
    }

    fun save(language: AppLanguage) {
        prefs.edit().putString("language", language.name).apply()
    }
}