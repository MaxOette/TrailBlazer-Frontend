package de.max.trailblazerfrontendv1.Util.datastore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

@Composable
fun dataStoreReader(applicationContext: Context, key: String, initial: Boolean): Boolean {
    val dataStore = DataStoreSingleton.DataStoreManager.getDataStore(applicationContext)

    val SETTING_KEY = booleanPreferencesKey(key)

    val darkModeEnabledFlow: Flow<Boolean> = dataStore.data
        .catch { exception  ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SETTING_KEY] ?: false
        }

    val setting by darkModeEnabledFlow.collectAsState(initial = initial)
    return setting
}