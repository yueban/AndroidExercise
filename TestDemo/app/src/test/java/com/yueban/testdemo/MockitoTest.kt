package com.yueban.testdemo

import android.content.Context
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-02-11
 */
@RunWith(MockitoJUnitRunner::class)
class MockitoTest {
    @Mock
    private lateinit var mockContext: Context

    @Test
    fun readStringFromContext_LocalizedString() {
        `when`(mockContext.getString(R.string.app_name)).thenReturn("Hello World")

        val appName = mockContext.getString(R.string.app_name)
//        assertThat(appName, `is`("TestDemo"))
        assertThat(appName, `is`("Hello World"))
    }
}