package co.binary.exploregithubandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
internal fun ExploreGitHubApp(modifier: Modifier = Modifier) {
    ExploreGitHubNavHost(
        modifier = modifier,
        navController = rememberNavController(),
    )
}
