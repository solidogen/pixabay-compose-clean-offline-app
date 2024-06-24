package com.example.pixabay.domain

import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.repository.ImagesRepository
import com.example.pixabay.domain.usecase.GetImageByIdUseCase
import com.example.pixabay.domain.utils.DataError
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetImageByIdUseCaseTest {

    private val imagesRepository: ImagesRepository = mockk()
    private lateinit var useCase: GetImageByIdUseCase

    @Before
    fun setup() {
        useCase = GetImageByIdUseCase(imagesRepository)
    }

    @Test
    fun `execute emits loading state initially`() = runTest {
        val id = "1"
        coEvery { imagesRepository.getImageById(id) } returns null

        val result = useCase.execute(id).toList()

        assertThat(result[0].isLoading).isTrue()
    }

    @Test
    fun `execute emits success state when image is found`() = runTest {
        val id = "2"
        val image = ImageModel(id, "user", "tags", "thumb", "large", 1, 2, 3)
        coEvery { imagesRepository.getImageById(id) } returns image

        val results = useCase.execute(id).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].isLoading).isTrue()
        assertThat(results[1].data).isEqualTo(image)
        assertThat(results[1].error).isNull()
    }

    @Test
    fun `execute emits error state when image is not found`() = runTest {
        val id = "3"
        coEvery { imagesRepository.getImageById(id) } returns null

        val results = useCase.execute(id).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].isLoading).isTrue()
        assertThat(results[1].error).isEqualTo(DataError.UnknownError)
    }

    @Test
    fun `execute emits error state when exception occurs`() = runTest {
        val id = "4"
        coEvery { imagesRepository.getImageById(id) } throws Exception("Test Exception")

        val results = useCase.execute(id).toList()

        assertThat(results).hasSize(2)
        assertThat(results[0].isLoading).isTrue()
        assertThat(results[1].error).isEqualTo(DataError.UnknownError)
    }
}