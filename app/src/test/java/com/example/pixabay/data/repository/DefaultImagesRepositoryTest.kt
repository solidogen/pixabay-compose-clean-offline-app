package com.example.pixabay.data.repository

import com.example.pixabay.data.cache.ImagesDao
import com.example.pixabay.data.cache.model.ImageEntity
import com.example.pixabay.data.network.ImagesApi
import com.example.pixabay.data.network.model.ImageDto
import com.example.pixabay.data.network.model.SearchImagesResponse
import com.example.pixabay.domain.model.ImageModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class DefaultImagesRepositoryTest {
    private val imagesApi: ImagesApi = mockk()
    private val imagesDao: ImagesDao = mockk()
    private lateinit var repository: DefaultImagesRepository

    @Before
    fun setup() {
        repository = DefaultImagesRepository(imagesApi, mockk(), imagesDao)
    }

    @Test
    fun `getCachedImagesList returns cached images when available`() = runTest {
        val query = "cats"
        val cachedImages = listOf(
            ImageEntity(
                id = "1",
                username = "user1",
                tags = "tags1",
                thumbnailUrl = "thumb1",
                largeImageUrl = "large1",
                likes = 1,
                downloads = 1,
                comments = 1,
                query = query
            ),
            ImageEntity(
                id = "2",
                username = "user2",
                tags = "tags2",
                thumbnailUrl = "thumb2",
                largeImageUrl = "large2",
                likes = 2,
                downloads = 2,
                comments = 2,
                query = query
            )
        )
        coEvery { imagesDao.getImagesByQuery(query) } returns cachedImages

        val result = repository.getCachedImagesList(query)

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("1")
        assertThat(result[1].id).isEqualTo("2")
    }

    @Test(expected = IllegalStateException::class)
    fun `getCachedImagesList throws error when no cached images available`() = runTest {
        val query = "dogs"
        coEvery { imagesDao.getImagesByQuery(query) } returns emptyList()

        repository.getCachedImagesList(query)
    }

    @Test
    fun `getFreshImagesList fetches and maps images from API`() = runTest {
        val query = "nature"
        val apiResponse = SearchImagesResponse(
            listOf(
                ImageDto(
                    id = "3",
                    username = "user3",
                    tags = "tags3",
                    thumbnailUrl = "thumb3",
                    largeImageUrl = "large3",
                    likes = 3,
                    downloads = 3,
                    comments = 3,
                ),
                ImageDto(
                    id = "4",
                    username = "user4",tags = "tags4",
                    thumbnailUrl = "thumb4",
                    largeImageUrl = "large4",
                    likes = 4,
                    downloads = 4,
                    comments = 4,
                ),
            )
        )
        coEvery { imagesApi.searchImages(query) } returns Response.success(apiResponse)

        val result = repository.getFreshImagesList(query)

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("3")
        assertThat(result[1].id).isEqualTo("4")
    }

    @Test
    fun `cacheImagesList inserts or replaces images in DAO`() = runTest {
        val images = listOf(
            ImageModel(
                id = "5",
                username = "user5",
                tagsString = "tags5",
                thumbnailUrl = "thumb5",
                largeImageUrl = "large5",
                likes = 5,
                downloads = 5,
                comments = 5,
            ),
            ImageModel(
                id = "6",
                username = "user6",
                tagsString = "tags6",
                thumbnailUrl = "thumb6",
                largeImageUrl = "large6",
                likes = 6,
                downloads = 6,
                comments = 6,
            )
        )
        coEvery { imagesDao.insertOrReplaceImages(any()) } just runs
        val query = "landscapes"

        repository.cacheImagesList(images, query)

        coVerify { imagesDao.insertOrReplaceImages(any()) }
    }

    @Test
    fun `getImageById returns image from DAO when available`() = runTest {
        val id = "7"
        val imageEntity = ImageEntity(
            id = "7",
            username = "user7",
            tags = "tags7",
            thumbnailUrl = "thumb7",
            largeImageUrl = "large7",
            likes = 7,
            downloads = 7,
            comments = 7,
            query = "query"
        )
        coEvery { imagesDao.getImageById(id) } returns imageEntity

        val result = repository.getImageById(id)

        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(id)
    }

    @Test
    fun `getImageById returns null when image not found in DAO`() = runTest {
        val id = "8"
        coEvery { imagesDao.getImageById(id) } returns null

        val result = repository.getImageById(id)

        assertThat(result).isNull()
    }
}