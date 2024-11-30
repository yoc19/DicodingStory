package com.dicoding.picodiploma.loginwithanimation.view.signup

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
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupView()
        registerValidation()

        val loadingDialog = AlertDialog.Builder(this).setView(R.layout.dialog_builder).create()

        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> loadingDialog.show()
                is ResultState.Success -> {
                    loadingDialog.dismiss()
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Yeah!")
                        setMessage(result.data.message)
                        setPositiveButton(getString(R.string.login)) { _, _ ->
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is ResultState.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun registerValidation() {
        with(binding) {
            signupButton.setOnClickListener {
                if (setupValidation()) {
                    viewModel.register(
                        edRegisterName.text.toString(),
                        edRegisterEmail.text.toString(),
                        edRegisterPassword.text.toString()
                    )

                } else {
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Yah!..")
                        setMessage("Coba cek data kamu lagi ya")
                        setPositiveButton("Cek lagi") { _, _ ->

                        }
                        create()
                        show()
                    }
                }
            }
        }
    }


    private fun setupValidation(): Boolean {
        with(binding) {
            return edRegisterPassword.error == null && !edRegisterPassword.text.isNullOrBlank() && edRegisterEmail.error == null && !edRegisterEmail.text.isNullOrBlank() && !edRegisterName.text.isNullOrBlank()
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
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(duration)
        val edRegisterName =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(duration)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(duration)
        val edRegisterEmail =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(duration)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(duration)
        val edRegisterPassword =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(duration)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(duration)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                edRegisterName,
                emailTextView,
                edRegisterEmail,
                passwordTextView,
                edRegisterPassword,
                signup
            )
            startDelay = 500
        }.start()
    }
}