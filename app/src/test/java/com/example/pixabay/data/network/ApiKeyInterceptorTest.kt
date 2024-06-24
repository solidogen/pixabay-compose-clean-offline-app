package com.example.pixabay.data.network

import com.example.pixabay.data.network.ApiKeyInterceptor.Companion.API_KEY
import com.example.pixabay.data.network.ApiKeyInterceptor.Companion.API_KEY_PARAMETER
import com.google.common.truth.Truth.assertThat
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import java.util.concurrent.TimeUnit

class ApiKeyInterceptorTest {
    private val interceptor = ApiKeyInterceptor()

    @Test
    fun `intercept adds API key to request`() {
        val originalRequest = Request.Builder()
            .url("https://example.com")
            .build()

        val chain = chain(originalRequest)
        val response = interceptor.intercept(chain)
        val newUrl = response.request.url

        assertThat(newUrl.queryParameter(API_KEY_PARAMETER)).isEqualTo(API_KEY)
    }

    private fun chain(originalRequest: Request) = object : Interceptor.Chain {

        override fun request(): Request = originalRequest

        override fun proceed(request: Request): Response =
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK").build()

        override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
            TODO("Not yet implemented")
        }

        override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
            TODO("Not yet implemented")
        }

        override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
            TODO("Not yet implemented")
        }

        override fun writeTimeoutMillis(): Int {
            TODO("Not yet implemented")
        }

        override fun call(): Call {
            TODO("Not yet implemented")
        }

        override fun connectTimeoutMillis(): Int {
            TODO("Not yet implemented")
        }

        override fun connection(): Connection? {
            TODO("Not yet implemented")
        }

        override fun readTimeoutMillis(): Int {
            TODO("Not yet implemented")
        }
    }
}