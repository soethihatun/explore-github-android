package co.binary.exploregithubandroid.core.network.datasource

import co.binary.exploregithubandroid.core.network.di.AccessToken
import co.binary.exploregithubandroid.core.network.getResult
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse
import co.binary.exploregithubandroid.core.network.service.GitHubService
import javax.inject.Inject

internal class GitHubUserNetworkDataSourceImpl @Inject constructor(
    @AccessToken private val accessToken: String,
    private val service: GitHubService,
    // TODO: Add dispatcher later
) : GitHubUserNetworkDataSource {
    override suspend fun searchUsers(query: String): Result<SearchGitHubUserResponse> {
        return getResult { service.searchUsers(accessToken, query) }
    }
}
