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

    const val USER_DETAIL_ROUTE = "$ROUTE/{${UserDetailNavigationArgs.USERNAME}}"
    fun userDetailRoute(username: String) = "$ROUTE/$username"
}

object UserDetailNavigationArgs {
    const val USERNAME = "username"
}

internal class UserDetailArgs(val username: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(username = checkNotNull(savedStateHandle[UserDetailNavigationArgs.USERNAME]))
}

fun NavGraphBuilder.homeNavGraph(navigateToUserDetail: (username: String) -> Unit, onBackClick: () -> Unit) {
    navigation(
        route = HomeNavigation.GRAPH_ROUTE,
        startDestination = HomeNavigation.ROUTE
    ) {
        homeScreen(goToUserDetail = navigateToUserDetail)

        userDetailScreen(onBackClick = onBackClick)
    }
}

private fun NavGraphBuilder.homeScreen(
    goToUserDetail: (username: String) -> Unit,
) {
    composable(route = HomeNavigation.ROUTE) {
        SearchGitHubUserRoute(goToUserDetail = goToUserDetail)
    }
}

private fun NavGraphBuilder.userDetailScreen(onBackClick: () -> Unit) {
    composable(route = HomeNavigation.USER_DETAIL_ROUTE) {
        GitHubUserDetailRoute(onBackClick = onBackClick)
    }
}

fun NavController.navigateToUserDetail(username: String) = navigate(userDetailRoute(username))
