package de.max.trailblazerfrontendv1.Util.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

class DataStoreSingleton {
    object DataStoreManager {
        private lateinit var dataStore: DataStore<Preferences>

        fun getDataStore(context: Context): DataStore<Preferences> {
            if (!DataStoreManager::dataStore.isInitialized) {
                dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("trailblazer_user_settings")
                }
            }
            return dataStore
        }
    }
}