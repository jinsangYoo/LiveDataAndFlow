package com.example.livedataandflow.image

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

const val DEFAULT_MAX_ITEMS = 2

class PickImages(
    maxItems: Int = DEFAULT_MAX_ITEMS
) : ActivityResultContracts.PickMultipleVisualMedia(maxItems) {
    override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
        val intent = super.createIntent(context, input)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        return intent
    }
}