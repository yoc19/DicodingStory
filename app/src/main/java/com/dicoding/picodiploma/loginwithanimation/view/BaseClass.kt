package com.dicoding.picodiploma.loginwithanimation.view

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R

open class BaseClass:AppCompatActivity() {
    fun setActionBar(){
        supportActionBar?.setCustomView(R.layout.action_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.navy)))
    }
}