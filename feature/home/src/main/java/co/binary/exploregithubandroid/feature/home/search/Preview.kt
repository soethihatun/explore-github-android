package co.binary.exploregithubandroid.feature.home.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.binary.exploregithubandroid.core.model.user.GitHubUser

private val dummyUser = GitHubUser(
    id = 1,
    username = "johndoe",
    avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
)

private val dummyUsers = listOf(
    dummyUser,
    dummyUser.copy(id = 2),
    dummyUser.copy(id = 3),
)

internal class SearchGitHubUserUiStateProvider : PreviewParameterProvider<SearchGitHubUserUiState> {
    override val values: Sequence<SearchGitHubUserUiState> = sequenceOf(
        SearchGitHubUserUiState.Success(
            queryText = "",
            users = dummyUsers,
            shouldLoadMore = true,
            loadingMore = false
        ),
        SearchGitHubUserUiState.Initial(),
        SearchGitHubUserUiState.Empty(),
        SearchGitHubUserUiState.Error(error = Exception()),
        SearchGitHubUserUiState.Loading(),
    )
}
