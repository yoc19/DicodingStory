package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.local.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DefaultResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultState<LoginResponse>>()
    val loginResult = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginResult.value = ResultState.Loading
                val response = repository.login(email, password)
                if (response.loginResult.token.isNotEmpty()) {
                    repository.saveSession(
                        UserModel(
                            response.loginResult.userId,
                            response.loginResult.name,
                            response.loginResult.token
                        )
                    )
                    _loginResult.value = ResultState.Success(response)
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                val errorMessage = errorBody.message
                _loginResult.value = ResultState.Error(errorMessage)
            }

        }
    }

}