package com.dicoding.picodiploma.loginwithanimation.data.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.local.StoryDatabase
import com.dicoding.picodiploma.loginwithanimation.data.local.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.local.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        return StoryRepository.getInstance(pref,apiService,database)
    }
}