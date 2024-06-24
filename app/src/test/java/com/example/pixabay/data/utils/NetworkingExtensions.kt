package com.example.pixabay.data.utils

import com.google.common.truth.Truth.assertThat
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class NetworkingExtensionsTest {

    @Test
    fun `bodyOrThrow returns body for successful response`() {
        val response: Response<String> = Response.success("Success")
        val result = response.bodyOrThrow()

        assertThat(result).isEqualTo("Success")
    }

    @Test(expected = IllegalStateException::class)
    fun `bodyOrThrow throws exception for unsuccessful response with error body`() {
        val response: Response<String> = Response.error(400, "Error Body".toResponseBody())
        response.bodyOrThrow()
    }

    @Test(expected = IllegalStateException::class)
    fun `bodyOrThrow throws exception for unsuccessful response without error body`() {
        val response: Response<String> = Response.error(404, "".toResponseBody())
        response.bodyOrThrow()
    }
}