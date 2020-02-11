package com.yueban.testdemo

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocaleTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun readStringFromContext_LocalizedString() {
        val appName = context.getString(R.string.app_name)
        assertThat(appName).isEqualTo("TestDemo")
    }
}