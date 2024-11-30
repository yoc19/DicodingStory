package com.dicoding.picodiploma.loginwithanimation.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentHomeBinding
import com.dicoding.picodiploma.loginwithanimation.helper.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class HomeFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getStories().observe(viewLifecycleOwner) { stories ->
            when (stories) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val adapter = StoryAdapter()
                    adapter.submitList(stories.data)
                    binding.rvStory.adapter = adapter
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.e("ResultActivity", "Error: $stories")
                    Toast.makeText(
                        requireContext(), "Load Articles Error: $stories", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}