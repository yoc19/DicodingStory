package com.dicoding.picodiploma.loginwithanimation.view.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentSettingBinding
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner){ result->
            binding.tvName.text = result.name
        }

        binding.actionLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.actionLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(),WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}