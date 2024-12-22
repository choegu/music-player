package com.choegozip.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.choegozip.domain.model.ComponentInfo
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.mediaControllerDataStore by preferencesDataStore(
    "media_controller_datastore"
)

class MediaControllerDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        private val KEY_UI_COMPONENT = stringPreferencesKey("ui_component")
    }

    suspend fun setUiComponent(uiComponentInfo: ComponentInfo) {
        context.mediaControllerDataStore.edit { pref ->
            pref[KEY_UI_COMPONENT] = Gson().toJson(uiComponentInfo)
        }
    }

    suspend fun getUiComponent(): ComponentInfo? {
        return context.mediaControllerDataStore.data.map {
            Gson().fromJson(it[KEY_UI_COMPONENT], ComponentInfo::class.java)
        }.firstOrNull()
    }

    suspend fun clearUiComponent() {
        context.mediaControllerDataStore.edit { it.clear() }
    }
}