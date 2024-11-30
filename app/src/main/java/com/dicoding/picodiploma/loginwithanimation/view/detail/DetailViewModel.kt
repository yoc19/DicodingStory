package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DefaultResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.Story
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _result = MutableLiveData<ResultState<Story>>()
    val result = _result


    fun getDetailStory(id:String){
        viewModelScope.launch {
            try {
                _result.value = ResultState.Loading
                val response = repository.getDetailStories(id)
                if(!response.error){
                    _result.value = ResultState.Success(response.story)
                }
            }catch (e:HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                val errorMessage = errorBody.message
                _result.value = ResultState.Error(errorMessage)
            }catch (e:Exception){
                _result.value = ResultState.Error(e.message.toString())
            }
        }
    }
}