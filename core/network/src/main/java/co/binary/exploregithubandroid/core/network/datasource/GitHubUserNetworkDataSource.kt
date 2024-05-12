package co.binary.exploregithubandroid.core.network.datasource

import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse

interface GitHubUserNetworkDataSource {
    suspend fun searchUsers(query: String): Result<SearchGitHubUserResponse>
}
