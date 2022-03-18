package com.yueban.jetpack

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.yueban.jetpack.ui.overview.OverviewBody
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeRule.setContent {
            OverviewBody()
        }
        composeRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()
    }
}