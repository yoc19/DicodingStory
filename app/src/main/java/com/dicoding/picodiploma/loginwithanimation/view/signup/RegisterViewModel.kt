package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DefaultResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<ResultState<DefaultResponse>>()
    val registerResult = _registerResult

    fun register(name:String,email: String,password: String){
        viewModelScope.launch {
            try {
                _registerResult.value = ResultState.Loading
                val response = repository.register(name, email, password)
                if(!response.error)
                    _registerResult.value = ResultState.Success(response)
            }catch (e : HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                val errorMessage = errorBody.message
                _registerResult.value = ResultState.Error(errorMessage)
            }

        }
    }
}