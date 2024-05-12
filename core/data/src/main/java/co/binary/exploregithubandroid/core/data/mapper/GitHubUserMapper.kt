package co.binary.exploregithubandroid.core.data.mapper

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserItemResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse

internal fun SearchGitHubUserResponse.asExternalModel(): List<GitHubUser> =
    items.map { it.asExternalModel() }

internal fun SearchGitHubUserItemResponse.asExternalModel(): GitHubUser =
    GitHubUser(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        name = ""
    )
