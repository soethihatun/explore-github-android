package co.binary.exploregithubandroid.core.data.user.mapper

import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserItemResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserResponse

internal fun SearchGitHubUserResponse.asExternalModel(): List<GitHubUser> =
    items.map { it.asExternalModel() }

internal fun SearchGitHubUserItemResponse.asExternalModel(): GitHubUser =
    GitHubUser(
        id = id,
        username = login,
        avatarUrl = avatarUrl,
    )

internal fun GitHubUserDetailResponse.asExternalModel(repos: List<GitHubUserRepo>): GitHubUserDetail =
    GitHubUserDetail(
        id = id,
        username = login,
        avatarUrl = avatarUrl,
        name = name,
        followers = followers,
        following = following,
        repos = repos,
    )

internal fun GitHubUserRepoResponse.asExternalModel(): GitHubUserRepo = GitHubUserRepo(
    id = id,
    name = name,
    description = description,
    stargazersCount = stargazersCount,
    primaryLanguage = language,
    htmlUrl = htmlUrl,
)
