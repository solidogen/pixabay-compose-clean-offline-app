package com.example.pixabay.domain.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DataStateTest {

    @Test
    fun `success creates DataState with correct data`() {
        val data = "Test Data"
        val state = DataState.success(data)
        assertThat(state.data).isEqualTo(data)
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `error creates DataState with correct error and cached data`() {
        val error = DataError.ErrorMessage("Error Message")
        val cachedData = "Cached Data"
        val state = DataState.error(error, cachedData)

        assertThat(state.data).isEqualTo(cachedData)
        assertThat(state.error).isEqualTo(error)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `error creates DataState with correct error and no cached data`() {
        val error = DataError.UnknownError
        val state = DataState.error<Any>(error)

        assertThat(state.data).isNull()
        assertThat(state.error).isEqualTo(error)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `loading creates DataState with correct loading state and cached data`() {
        val cachedData = "Cached Data"
        val state = DataState.loading(cachedData)

        assertThat(state.data).isEqualTo(cachedData)
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `loading creates DataState with correct loading state and no cached data`() {
        val state = DataState.loading<Any>()

        assertThat(state.data).isNull()
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isTrue()
    }
}