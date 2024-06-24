package com.example.pixabay.data.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ComposeImmutableListTest {

    @Test(expected = ClassCastException::class)
    @Suppress("UNCHECKED_CAST")
    fun `from creates immutable list`() {
        val originalList = listOf(1, 2, 3)
        val immutableList = ComposeImmutableList.from(originalList)

        assertThat(immutableList).containsExactlyElementsIn(originalList).inOrder()

        // Try to modify the list (should throw an exception)
        (immutableList as MutableList<Int>)[0] = 4
    }

    @Test
    fun `list operations work as expected`() {
        val immutableList = ComposeImmutableList.from(listOf("a", "b", "c"))

        assertThat(immutableList.size).isEqualTo(3)
        assertThat(immutableList[1]).isEqualTo("b")
        assertThat(immutableList.contains("c")).isTrue()
        assertThat(immutableList.indexOf("b")).isEqualTo(1)
    }
}