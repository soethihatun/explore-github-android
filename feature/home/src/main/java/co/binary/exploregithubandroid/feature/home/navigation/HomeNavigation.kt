package co.binary.exploregithubandroid.feature.home.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import co.binary.exploregithubandroid.feature.home.navigation.HomeNavigation.userDetailRoute
import co.binary.exploregithubandroid.feature.home.search.SearchGitHubUserRoute
import co.binary.exploregithubandroid.feature.home.user.GitHubUserDetailRoute

object HomeNavigation {
    const val GRAPH_ROUTE = "home_graph_route"
    const val ROUTE = "home_route"

    const val USER_DETAIL_ROUTE = "$ROUTE/{${UserDetailNavigationArgs.LOGIN}}"
    fun userDetailRoute(login: String) = "$ROUTE/$login"
}

object UserDetailNavigationArgs {
    const val LOGIN = "login"
}

internal class UserDetailArgs(val login: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(login = checkNotNull(savedStateHandle[UserDetailNavigationArgs.LOGIN]))
}

fun NavGraphBuilder.homeNavGraph(navigateToUserDetail: (login: String) -> Unit) {
    navigation(
        route = HomeNavigation.GRAPH_ROUTE,
        startDestination = HomeNavigation.ROUTE
    ) {
        homeScreen(goToUserDetail = navigateToUserDetail)

        userDetailScreen()
    }
}

private fun NavGraphBuilder.homeScreen(
    goToUserDetail: (login: String) -> Unit,
) {
    composable(route = HomeNavigation.ROUTE) {
        SearchGitHubUserRoute(goToUserDetail = goToUserDetail)
    }
}

private fun NavGraphBuilder.userDetailScreen() {
    composable(route = HomeNavigation.USER_DETAIL_ROUTE) {
        GitHubUserDetailRoute()
    }
}

fun NavController.navigateToUserDetail(login: String) = navigate(userDetailRoute(login))
