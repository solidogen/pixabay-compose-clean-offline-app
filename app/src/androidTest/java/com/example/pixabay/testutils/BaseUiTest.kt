package com.example.pixabay.testutils

import androidx.annotation.IdRes
import androidx.test.platform.app.InstrumentationRegistry

interface BaseUiTest {
    fun getString(@IdRes id: Int) = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .getString(id)

    fun getString(@IdRes id: Int, vararg args: Any?) = InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .getString(id, *args)
}