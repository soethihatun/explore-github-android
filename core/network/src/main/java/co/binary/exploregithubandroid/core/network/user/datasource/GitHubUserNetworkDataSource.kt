package co.binary.exploregithubandroid.core.network.user.datasource

import co.binary.exploregithubandroid.core.network.user.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserResponse

interface GitHubUserNetworkDataSource {
    suspend fun searchGitHubUsers(query: String, page: Int): Result<SearchGitHubUserResponse>

    suspend fun getGitHubUserDetail(username: String): Result<GitHubUserDetailResponse>

    suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubUserRepoResponse>>
}
