package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupView()
        loginValidation()
        observeViewModel()
    }

    private fun observeViewModel() {
        val loadingDialog = AlertDialog.Builder(this).setView(R.layout.dialog_builder).create()
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> loadingDialog.show()
                is ResultState.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                is ResultState.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginValidation() {
        with(binding) {
            loginButton.setOnClickListener {
                if (setupValidation()) {
                    viewModel.login(edLoginEmail.text.toString(), edLoginPassword.text.toString())
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.validation_failed), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun setupValidation(): Boolean {
        with(binding) {
            return edLoginPassword.error == null && !edLoginPassword.text.isNullOrBlank() && edLoginEmail.error == null && !edLoginEmail.text.isNullOrBlank()
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val duration :Long = 350

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(duration)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(duration)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(duration)
        val edLoginEmail =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(duration)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(duration)
        val edLoginPassword =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(duration)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(duration)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                edLoginEmail,
                passwordTextView,
                edLoginPassword,
                login
            )
            startDelay = 100
        }.start()
    }
}