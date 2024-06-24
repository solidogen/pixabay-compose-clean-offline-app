package com.example.pixabay.domain

import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.repository.ImagesRepository
import com.example.pixabay.domain.usecase.GetImageListUseCase
import com.example.pixabay.domain.utils.DataError
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetImageListUseCaseTest {

    private val imagesRepository: ImagesRepository = mockk()
    private lateinit var useCase: GetImageListUseCase

    @Before
    fun setup() {
        useCase = GetImageListUseCase(imagesRepository)
    }

    @Test
    fun `execute emits success with cached images when available`() = runTest {
        val query = "cats"
        val cachedImages = listOf(
            ImageModel("1", "user1", "tags1", "thumb1", "large1", 1, 2, 3),
            ImageModel("2", "user2", "tags2", "thumb2", "large2", 4, 5, 6)
        )
        coEvery { imagesRepository.getCachedImagesList(query) } returns cachedImages
        coEvery { imagesRepository.getFreshImagesList(query) } returns emptyList() // Not used in this test

        val results = useCase.execute(query).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].isLoading).isFalse() // Initial loading state with cached data
        assertThat(results[0].data).isEqualTo(cachedImages)
        assertThat(results[0].error).isNull()
    }

    @Test
    fun `execute emits loading then success with fresh images when no cached images`() = runTest {
        val query = "dogs"
        val freshImages = listOf(
            ImageModel("3", "user3", "tags3", "thumb3", "large3", 7, 8, 9)
        )
        coEvery { imagesRepository.getCachedImagesList(query) } throws Exception("No cached images")
        coEvery { imagesRepository.getFreshImagesList(query) } returns freshImages
        coEvery { imagesRepository.cacheImagesList(freshImages, query) } just runs

        val results = useCase.execute(query).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].isLoading).isTrue() // Loading state while fetching fresh images
        assertThat(results[1].data).isEqualTo(freshImages)
        assertThat(results[1].error).isNull()
    }

    @Test
    fun `execute emits error with cached images when fetching fresh images fails`() = runTest {
        val query = "birds"
        val cachedImages = listOf(
            ImageModel("4", "user4", "tags4", "thumb4", "large4", 10, 11, 12)
        )
        coEvery { imagesRepository.getCachedImagesList(query) } returns cachedImages
        coEvery { imagesRepository.getFreshImagesList(query) } throws Exception("Network error")

        val results = useCase.execute(query).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].data).isEqualTo(cachedImages) // Cached data is still available
        assertThat(results[1].error).isEqualTo(DataError.ErrorMessage("Network error"))
    }

    @Test
    fun `execute emits error when both cached and fresh image fetching fails`() = runTest {
        val query = "fish"
        coEvery { imagesRepository.getCachedImagesList(query) } throws Exception("No cached images")
        coEvery { imagesRepository.getFreshImagesList(query) } throws Exception("Network error")

        val results = useCase.execute(query).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].isLoading).isTrue() // Initial loading state with no cached data
        assertThat(results[1].error).isEqualTo(DataError.ErrorMessage("Network error"))
    }
}