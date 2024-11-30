package com.dicoding.picodiploma.loginwithanimation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        splashScreen.setKeepOnScreenCondition{true}
        supportActionBar?.hide()
        viewModel.getSession().observe(this) { user ->
            if (user.token.isBlank()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }
}