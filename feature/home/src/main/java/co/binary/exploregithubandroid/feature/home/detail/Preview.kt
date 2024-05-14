package co.binary.exploregithubandroid.feature.home.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo

private val dummyRepo = GitHubUserRepo(
    id = 1,
    name = "Lorem Ipsum Repo",
    description = "Lorem Ipsum description",
    primaryLanguage = "Kotlin",
    htmlUrl = "https://github.com/JetBrains/kotlin",
    stargazersCount = 1,
)

private val dummyRepos = listOf(
    dummyRepo,
    dummyRepo.copy(id = 2),
    dummyRepo.copy(id = 3),
)

private val dummyUserDetail = GitHubUserDetail(
    id = 1,
    username = "johndoe",
    avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
    name = "John Doe",
    followers = 1,
    following = 1,
    repos = dummyRepos,
)

internal class GitHubUserDetailUiStateProvider : PreviewParameterProvider<GitHubUserDetailUiState> {
    override val values: Sequence<GitHubUserDetailUiState> = sequenceOf(
        GitHubUserDetailUiState.Success(dummyUserDetail, 1),
        GitHubUserDetailUiState.Loading,
        GitHubUserDetailUiState.Error,
    )
}
