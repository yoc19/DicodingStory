package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentUploadBinding
import com.dicoding.picodiploma.loginwithanimation.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.helper.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.helper.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.camera.CameraActivity
import com.dicoding.picodiploma.loginwithanimation.view.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity.Companion.REQUIRED_PERMISSION


class UploadFragment : Fragment() {
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private lateinit var binding: FragmentUploadBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        showImage()
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text
            val loadingDialog = AlertDialog.Builder(requireContext()).setView(R.layout.dialog_builder).create()

            viewModel.uploadImage(imageFile, description.toString()).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            loadingDialog.show()
                        }

                        is ResultState.Success -> {
                            showToast(result.data.message)
                            loadingDialog.dismiss()
                            findNavController().navigate(R.id.action_navigation_camera_to_navigation_home)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            loadingDialog.dismiss()
                        }
                    }
                }
            }

    } ?:showToast(getString(R.string.empty_image))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            viewModel.currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }
    private fun showImage() {
        currentImageUri = viewModel.currentImageUri
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPhoto.setImageURI(it)
        }
    }
}