package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.local.StoryDatabase
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.data.local.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.local.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DefaultResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String) = apiService.login(email, password)


    suspend fun getWidgetStories() = storyDatabase.storyDao().getWidgetStory()

    fun getStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,userPreference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }



    suspend fun getDetailStories(id: String): DetailResponse {
        val token = "Bearer ${userPreference.getToken().first()}"
        return apiService.getDetailStory(token,id)
    }

    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun uploadImage(imageFile: File, description: String,lat :String?,lon:String?) = liveData {
        val token = "Bearer ${userPreference.getToken().first()}"
        emit(ResultState.Loading)
        val descRequestBody = description.toRequestBody("text/plain".toMediaType())
        val latRequestBody = lat?.toRequestBody("text/plain".toMediaType())
        val lonRequestBody = lon?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(token,multipartBody, descRequestBody,latRequestBody,lonRequestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }catch (e:Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    suspend fun getStoriesWithLocation()= apiService.getStoriesWithLocation("Bearer ${userPreference.getToken().first()}")

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService, storyDatabase: StoryDatabase
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(userPreference, apiService, storyDatabase)
        }.also { instance = it }
    }
}

