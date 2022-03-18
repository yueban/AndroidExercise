package com.yueban.jetpack

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.yueban.jetpack.ui.components.RallyTopAppBar
import com.yueban.jetpack.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        composeRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = RallyScreen.values().toList(),
                    onTabSelected = {},
                    currentScreen = RallyScreen.Accounts
                )
            }
        }
        composeRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        composeRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = RallyScreen.values().toList(),
                    onTabSelected = {},
                    currentScreen = RallyScreen.Accounts
                )
            }
        }

        composeRule
//            .onNodeWithText(text = RallyScreen.Accounts.name.uppercase(), useUnmergedTree = true)
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase()) and
                    hasParent(
                        hasContentDescription(RallyScreen.Accounts.name)
                    ),
                useUnmergedTree = true
            )
            .assertExists()
    }
}