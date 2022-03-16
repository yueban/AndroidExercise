/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yueban.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.yueban.jetpack.data.UserData
import com.yueban.jetpack.ui.accounts.AccountsBody
import com.yueban.jetpack.ui.accounts.SingleAccountBody
import com.yueban.jetpack.ui.bills.BillsBody
import com.yueban.jetpack.ui.components.RallyTabRow
import com.yueban.jetpack.ui.overview.OverviewBody
import com.yueban.jetpack.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = RallyScreen.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen -> navController.navigate(screen.name) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            NavHost(navController = navController,
                    startDestination = RallyScreen.Overview.name,
                    Modifier.padding(innerPadding)) {
                composable(RallyScreen.Overview.name) {
                    OverviewBody(
                        onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                        onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                        onAccountClick = { navController.navigate("${RallyScreen.Accounts.name}/$it") }
                    )
                }
                composable(RallyScreen.Accounts.name) {
                    AccountsBody(
                        accounts = UserData.accounts,
                        onAccountClick = { navController.navigate("${RallyScreen.Accounts.name}/$it") })
                }
                composable(RallyScreen.Bills.name) {
                    BillsBody(bills = UserData.bills)
                }
                composable(
                    route = "${RallyScreen.Accounts.name}/{name}",
                    arguments = listOf(
                        navArgument("name") {
                            type = NavType.StringType
                        }
                    ),
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "rally://${RallyScreen.Accounts.name}/{name}"
                        }
                    )
                ) { entry: NavBackStackEntry ->
                    val name = entry.arguments?.getString("name")
                    val account = UserData.getAccount(name)
                    SingleAccountBody(account = account)
                }
            }
        }
    }
}
