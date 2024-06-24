package com.example.pixabay.data.utils

import androidx.compose.runtime.Immutable
import java.util.Collections

@Immutable
data class ComposeImmutableList<E> private constructor(
    private val baseList: List<E>
) : List<E> by baseList {
    companion object {
        /**
         * Creates [ComposeImmutableList] from [baseList].
         */
        fun <E> from(baseList: List<E>): ComposeImmutableList<E> {
            return ComposeImmutableList(Collections.unmodifiableList(ArrayList(baseList)))
        }
    }
}