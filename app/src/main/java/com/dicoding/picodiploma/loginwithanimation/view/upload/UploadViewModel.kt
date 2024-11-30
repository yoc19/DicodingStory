package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import java.io.File

class UploadViewModel(private val repository: UserRepository):ViewModel() {

    var currentImageUri: Uri?  = null

    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}