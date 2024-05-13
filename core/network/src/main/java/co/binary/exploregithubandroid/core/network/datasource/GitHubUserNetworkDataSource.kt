package co.binary.exploregithubandroid.core.network.datasource

import co.binary.exploregithubandroid.core.network.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse

interface GitHubUserNetworkDataSource {
    suspend fun searchUsers(query: String, page: Int): Result<SearchGitHubUserResponse>

    suspend fun getUserDetail(username: String): Result<GitHubUserDetailResponse>

    suspend fun getUserRepos(username: String): Result<List<GitHubUserRepoResponse>>
}
