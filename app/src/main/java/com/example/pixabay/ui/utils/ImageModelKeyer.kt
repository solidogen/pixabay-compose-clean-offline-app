package com.example.pixabay.ui.utils

import coil.key.Keyer
import coil.request.Options
import com.example.pixabay.domain.model.ImageModel

class ImageModelKeyer : Keyer<ImageModel> {
    override fun key(data: ImageModel, options: Options): String? {
        return data.id
    }
}