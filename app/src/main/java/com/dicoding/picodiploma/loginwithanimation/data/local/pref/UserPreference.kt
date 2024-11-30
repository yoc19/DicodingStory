package com.dicoding.picodiploma.loginwithanimation.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_NAME] = user.name
            preferences[USER_TOKEN] = user.token
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USER_ID] ?: "",
                preferences[USER_NAME] ?: "",
                preferences[USER_TOKEN] ?: ""
            )
        }
    }

    fun getToken():Flow<String?>{
        return dataStore.data.map { preferences ->
            preferences[USER_TOKEN]
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(USER_NAME)
            preferences.remove(USER_TOKEN)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_ID = stringPreferencesKey("id")
        private val USER_NAME = stringPreferencesKey("name")
        private val USER_TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}