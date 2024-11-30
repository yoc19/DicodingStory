package com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DefaultResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ):LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ):DefaultResponse


    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id:String
    ):DetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): DefaultResponse
}