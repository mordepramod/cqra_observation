package com.example.observationapp.repository.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.util.CommonConstant.DATASTORE_NAME
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DatastoreRepoImpl @Inject constructor(
    private val context: Context
) : DataStoreRepoInterface {
    override suspend fun putString(key: String, value: String) {
        context.dataStore.edit {
            it[getStringPrefKey(key)] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        context.dataStore.edit {
            it[getBooleanPrefKey(key)] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        context.dataStore.edit {
            it[getIntPrefKey(key)] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[getStringPrefKey(key)]
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[getBooleanPrefKey(key)]
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getInt(key: String): Int? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[getIntPrefKey(key)]
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearSharedPref(prefType: PreferenceType, key: String) {
        when (prefType) {
            PreferenceType.STRING_PREF -> {
                val prefKey = getStringPrefKey(key)
                context.dataStore.edit {
                    if (it.contains(prefKey)) {
                        it.remove(prefKey)
                    }
                }
            }

            PreferenceType.BOOLEAN_PREF -> {
                val prefKey = getBooleanPrefKey(key)
                context.dataStore.edit {
                    if (it.contains(prefKey)) {
                        it.remove(prefKey)
                    }
                }
            }

            PreferenceType.INT_PREF -> {
                val prefKey = getIntPrefKey(key)
                context.dataStore.edit {
                    if (it.contains(prefKey)) {
                        it.remove(prefKey)
                    }
                }
            }
        }

    }

    private fun getStringPrefKey(key: String): Preferences.Key<String> {
        return stringPreferencesKey(key)
    }

    private fun getBooleanPrefKey(key: String): Preferences.Key<Boolean> {
        return booleanPreferencesKey(key)
    }

    private fun getIntPrefKey(key: String): Preferences.Key<Int> {
        return intPreferencesKey(key)
    }

    enum class PreferenceType {
        STRING_PREF,
        BOOLEAN_PREF,
        INT_PREF,
    }
}