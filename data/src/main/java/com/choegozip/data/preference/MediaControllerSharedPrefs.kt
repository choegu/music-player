package com.choegozip.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.choegozip.domain.model.ComponentInfo
import com.google.gson.Gson
import javax.inject.Inject

class MediaControllerSharedPrefs @Inject constructor(
    context: Context
) {
    companion object {
        private const val PREF_NAME = "media_controller_prefs"
        private const val KEY_UI_COMPONENT = "ui_component"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * UI Component 저장
     */
    fun setUiComponent(uiComponentInfo: ComponentInfo) {
        sharedPreferences.edit().apply {
            putString(KEY_UI_COMPONENT, gson.toJson(uiComponentInfo))
            apply()
        }
    }

    /**
     * UI Component 가져오기
     */
    fun getUiComponent(): ComponentInfo? {
        val json = sharedPreferences.getString(KEY_UI_COMPONENT, null)
        return json?.let {
            gson.fromJson(it, ComponentInfo::class.java)
        }
    }

    /**
     * UI Component 삭제
     */
    fun clearUiComponent() {
        sharedPreferences.edit().apply {
            remove(KEY_UI_COMPONENT)
            apply()
        }
    }
}