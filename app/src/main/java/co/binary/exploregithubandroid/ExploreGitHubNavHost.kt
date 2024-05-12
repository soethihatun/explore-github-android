package co.binary.exploregithubandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import co.binary.exploregithubandroid.feature.home.navigation.HomeNavigation
import co.binary.exploregithubandroid.feature.home.navigation.homeNavGraph
import co.binary.exploregithubandroid.feature.home.navigation.navigateToUserDetail

@Composable
internal fun ExploreGitHubNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(modifier = modifier, navController = navController, startDestination = HomeNavigation.GRAPH_ROUTE) {
        homeNavGraph(navigateToUserDetail = { login -> navController.navigateToUserDetail(login) })
    }
}
