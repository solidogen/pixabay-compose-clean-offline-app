package com.example.pixabay.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder().addQueryParameter(API_KEY_PARAMETER, API_KEY).build()
        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val API_KEY_PARAMETER = "key"
        const val API_KEY = "44524347-8b65a252eee74d6cfc36037f8" // this should be at least hidden with NDK help
    }
}