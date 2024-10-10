package com.example.livedataandflow.image

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livedataandflow.R
import com.example.livedataandflow.databinding.ActivityPhotoPickerBinding

class PhotoPickerActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityPhotoPickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPhotoPickerBinding.inflate(layoutInflater).apply {

        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        binding.btnOneCamera.setOnClickListener {
            // Launch the photo picker and let the user choose images and videos.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.btnOneOnlyImageCamera.setOnClickListener {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnOneOnlyVideoCamera.setOnClickListener {
            // Launch the photo picker and let the user choose only videos.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
        }

        binding.btnOneOnlyImageOrGIfCamera.setOnClickListener {
            // Launch the photo picker and let the user choose only images/videos of a
            // specific MIME type, such as GIFs.
            val mimeType = "image/gif"
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
        }

        binding.btnMultiAllCamera.setOnClickListener {
            // For this example, launch the photo picker and let the user choose images
            // and videos. If you want the user to select a specific type of media file,
            // use the overloaded versions of launch(), as shown in the section about how
            // to select a single media item.
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.btnMultiOnlyImagesCamera.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.pickImage.setOnClickListener {
            pickImageMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/*")))
        }

        binding.pickImages.setOnClickListener {
            pickImageMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/*")))
        }
    }

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        binding.resultForMultiple.text = ""
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            binding.result.text = "Selected URI: $uri"
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            binding.result.text = "No media selected"
            Log.d("PhotoPicker", "No media selected")
        }
    }

    // Registers a photo picker activity launcher in multi-select mode.
    // In this example, the app lets the user select up to 5 media files.
    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            binding.resultForMultiple.text = ""
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                binding.result.text = "Number of items selected: ${uris.size}"
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                StringBuilder().apply {
                    append("Selected URIs:")
                    for (uri in uris) {
                        Log.d("PhotoPicker", "Selected URI: $uri")
                        append("\n$uri")
                    }
                    binding.resultForMultiple.text = toString()
                }
            } else {
                binding.result.text = "No media selected"
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val pickImageMedia = registerForActivityResult(PickImage()) { uri ->
        binding.resultForMultiple.text = ""
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            binding.result.text = "Selected URI: $uri"
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            binding.result.text = "No media selected"
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val pickImageMultipleMedia =
        registerForActivityResult(PickImages(5)) { uris ->
            binding.resultForMultiple.text = ""
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                binding.result.text = "Number of items selected: ${uris.size}"
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                StringBuilder().apply {
                    append("Selected URIs:")
                    for (uri in uris) {
                        Log.d("PhotoPicker", "Selected URI: $uri")
                        append("\n$uri")
                    }
                    binding.resultForMultiple.text = toString()
                }
            } else {
                binding.result.text = "No media selected"
                Log.d("PhotoPicker", "No media selected")
            }
        }
}