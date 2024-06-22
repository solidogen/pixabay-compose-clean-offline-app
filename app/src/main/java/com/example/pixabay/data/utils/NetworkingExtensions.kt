package com.example.pixabay.data.utils

import retrofit2.Response

fun <T> Response<T>.bodyOrThrow() =
    takeIf { it.isSuccessful }?.body() ?: error(errorBody()?.string().orEmpty())
