package com.yueban.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.yueban.jetpack.data.UserData
import com.yueban.jetpack.ui.accounts.AccountsBody
import com.yueban.jetpack.ui.accounts.SingleAccountBody
import com.yueban.jetpack.ui.bills.BillsBody
import com.yueban.jetpack.ui.overview.OverviewBody

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController,
            startDestination = RallyScreen.Overview.name,
            modifier = modifier) {
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