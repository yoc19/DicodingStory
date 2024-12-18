package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import java.io.File

class UploadViewModel(private val repository: StoryRepository):ViewModel() {

    var currentImageUri: Uri?  = null

    fun uploadImage(file: File, description: String,lat :String?,lon:String?) = repository.uploadImage(file, description,lat,lon)
}