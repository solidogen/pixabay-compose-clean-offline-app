package com.example.pixabay.ui.utils

import coil.key.Keyer
import coil.request.Options
import com.example.pixabay.domain.model.ImageModel

@Deprecated("Using ImageRequest settings inside LoadableAsyncImage")
class ImageModelKeyer : Keyer<ImageModel> {

    /**
     * Shortcut for getting the right cache key for the model.
     *
     * Might be useful in url regenerating scenario, but I don't
     * have that much time to explore [options] in detail.
     * */
    override fun key(data: ImageModel, options: Options): String {
        return data.id
    }
}