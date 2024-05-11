package co.binary.exploregithubandroid.feature.home.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.binary.exploregithubandroid.core.designsystem.theme.ExploreGitHubAndroidTheme

@Composable
internal fun SearchGitHubUserRoute(modifier: Modifier = Modifier) {
    SearchGitHubUserScreen(modifier = modifier)
}

@Composable
private fun SearchGitHubUserScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("Search the user")
    }
}

@Preview
@Composable
private fun SearchGitHubUserScreenPreview() {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen()
    }
}
