package com.example.pixabay.domain.utils

data class DataState<out T>(
    val data: T? = null,
    val error: DataError? = null,
    val isLoading: Boolean = false,
) {
    companion object {
        fun <T> success(data: T): DataState<T> = DataState(data = data)
        fun <T> error(error: DataError, cachedData: T? = null): DataState<T> =
            DataState(error = error, data = cachedData)
        fun <T> loading(cachedData: T? = null): DataState<T> =
            DataState(isLoading = true, data = cachedData)
    }
}

sealed class DataError {
    data object UnknownError : DataError()
    data class ErrorMessage(val message: String): DataError()
}