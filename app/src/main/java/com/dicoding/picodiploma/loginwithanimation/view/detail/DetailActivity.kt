package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.BaseClass

class DetailActivity : BaseClass() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(STORY_ID)

        if (id != null) viewModel.getDetailStory(id)

        setActionBar()
        observeViewModel()

    }

    private fun showDetail() {
        with(binding) {
            tvDetailName.visibility = View.VISIBLE
            ivDetailPhoto.visibility = View.VISIBLE
            tvDetailDescription.visibility = View.VISIBLE
            ivProfile.visibility = View.VISIBLE
        }
    }
    private fun hideDetail() {
        with(binding) {
            tvDetailName.visibility = View.GONE
            ivDetailPhoto.visibility = View.GONE
            tvDetailDescription.visibility = View.GONE
            ivProfile.visibility = View.GONE
        }
    }

    private fun observeViewModel() {
        viewModel.result.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    hideDetail()
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showDetail()
                    with(binding) {
                        tvDetailName.text = result.data.name
                        Glide.with(this@DetailActivity).load(result.data.photoUrl).apply(
                            RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error)
                        ).into(ivDetailPhoto)
                        tvDetailDescription.text = result.data.description
                    }
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    AlertDialog.Builder(this).apply {
                        setTitle(result.error.uppercase() + "!!")
                        setMessage(getString(R.string.detail_fail))
                        setPositiveButton("Back") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }

                else -> {}
            }
        }
    }

    companion object {
        const val STORY_ID = "event_id"
    }
}