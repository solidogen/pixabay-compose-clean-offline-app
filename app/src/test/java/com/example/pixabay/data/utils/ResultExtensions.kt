package com.example.pixabay.data.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class RunCatchingAsyncTest {

    @Test
    fun `runCatchingAsync returns success result for successful block`() =runTest {
        val result = 1.runCatchingAsync { this + 2 }

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(3)
    }

    @Test
    fun `runCatchingAsync returns failure result for exception`() = runTest {
        val result = 1.runCatchingAsync { throw IOException("Test Exception") }

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Test Exception")
    }

    @Test(expected = CancellationException::class)
    fun `runCatchingAsync rethrows CancellationException`() = runTest {
        1.runCatchingAsync { throw CancellationException("Test Cancellation") }
    }
}