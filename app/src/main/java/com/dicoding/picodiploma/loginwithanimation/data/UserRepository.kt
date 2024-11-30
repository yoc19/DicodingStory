package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.picodiploma.loginwithanimation.data.local.dao.StoryDao
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

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDao: StoryDao
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String) = apiService.login(email, password)


    suspend fun getWidgetStories() = storyDao.getWidgetStory()

    fun getStories(): LiveData<ResultState<List<StoryEntity>>> = liveData {
        emit(ResultState.Loading)
        try {
            val token = "Bearer ${userPreference.getToken().first()}"
            val response = apiService.getStory(token)
            val story = response.listStory
            val storyList = story.map { story ->
                StoryEntity(
                    id = story.id,
                    photoUrl = story.photoUrl,
                    createdAt = story.createdAt,
                    name = story.name,
                    description = story.description,
                )
            }
            storyDao.deleteAllStory()
            storyDao.insertStories(storyList)
        } catch (e: Exception) {
            Log.d("Repository", "getStories: ${e.message.toString()} ")
            emit(ResultState.Error(e.message.toString()))
        }
        val localData: LiveData<ResultState<List<StoryEntity>>> =
            storyDao.getStory().map { ResultState.Success(it) }
        emitSource(localData)
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

    fun uploadImage(imageFile: File, description: String) = liveData {
        val token = "Bearer ${userPreference.getToken().first()}"
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(token,multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }catch (e:Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService, storyDao: StoryDao
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService, storyDao)
        }.also { instance = it }
    }
}

