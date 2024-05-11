package co.binary.exploregithubandroid.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import co.binary.exploregithubandroid.feature.home.search.SearchGitHubUserRoute

object HomeNavigation {
    const val GRAPH_ROUTE = "home_graph_route"
    const val ROUTE = "home_route"
}

fun NavGraphBuilder.homeNavGraph() {
    navigation(
        route = HomeNavigation.GRAPH_ROUTE,
        startDestination = HomeNavigation.ROUTE
    ) {
        homeScreen()
    }
}

private fun NavGraphBuilder.homeScreen() {
    composable(route = HomeNavigation.ROUTE) {
        SearchGitHubUserRoute()
    }
}
