package com.example.pixabay.data.utils

import kotlinx.coroutines.CancellationException

/**
 * runCatching that doesn't have side effects to coroutine cancellation
 * */
suspend inline fun <T, R> T.runCatchingAsync(crossinline block: suspend T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}