package com.example.pixabay.domain.model

import com.example.pixabay.ui.utils.LARGE_IMAGE_CACHE_KEY_AFFIX
import com.example.pixabay.ui.utils.THUMBNAIL_CACHE_KEY_AFFIX
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ImageModelTest {

    private val imageModel = ImageModel(
        id = "123",
        username = "testUser",
        tagsString = "tag1, tag2, tag3",
        thumbnailUrl = "https://example.com/thumbnail.jpg",
        largeImageUrl = "https://example.com/large.jpg",
        likes = 100,
        downloads = 50,
        comments = 20
    )

    @Test
    fun `tagList is correctly parsed from tagsString`() {
        assertThat(imageModel.tagList).containsExactly("tag1", "tag2", "tag3").inOrder()
    }

    @Test
    fun `getCacheKey returns correct key for thumbnail`() {
        val thumbnailKey = imageModel.getCacheKey(isThumbnail =true)
        assertThat(thumbnailKey).isEqualTo("123$THUMBNAIL_CACHE_KEY_AFFIX")
    }

    @Test
    fun `getCacheKey returns correct key for large image`() {
        val largeImageKey = imageModel.getCacheKey(isThumbnail = false)
        assertThat(largeImageKey).isEqualTo("123$LARGE_IMAGE_CACHE_KEY_AFFIX")
    }

    @Test
    fun `getImageUrl returns thumbnail URL when isThumbnail is true`() {
        val imageUrl = imageModel.getImageUrl(isThumbnail = true)
        assertThat(imageUrl).isEqualTo(imageModel.thumbnailUrl)
    }

    @Test
    fun `getImageUrl returns large image URL when isThumbnail is false`() {
        val imageUrl = imageModel.getImageUrl(isThumbnail = false)
        assertThat(imageUrl).isEqualTo(imageModel.largeImageUrl)
    }
}