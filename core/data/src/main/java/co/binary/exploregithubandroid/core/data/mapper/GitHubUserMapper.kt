package co.binary.exploregithubandroid.core.data.mapper

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.network.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserItemResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse

internal fun SearchGitHubUserResponse.asExternalModel(): List<GitHubUser> =
    items.map { it.asExternalModel() }

internal fun SearchGitHubUserItemResponse.asExternalModel(): GitHubUser =
    GitHubUser(
        id = id,
        username = login,
        avatarUrl = avatarUrl,
    )

internal fun GitHubUserDetailResponse.asExternalModel(): GitHubUserDetail =
    GitHubUserDetail(
        id = id,
        username = login,
        avatarUrl = avatarUrl,
        name = name,
        followers = followers,
        following = following,
        repos = emptyList(),
    )
