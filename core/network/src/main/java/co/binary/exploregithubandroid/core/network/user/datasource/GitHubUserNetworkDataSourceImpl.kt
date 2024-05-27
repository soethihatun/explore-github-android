package co.binary.exploregithubandroid.core.network.user.datasource

import co.binary.exploregithubandroid.core.network.di.AccessToken
import co.binary.exploregithubandroid.core.network.getResult
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserResponse
import co.binary.exploregithubandroid.core.network.user.service.GitHubService
import javax.inject.Inject

internal class GitHubUserNetworkDataSourceImpl @Inject constructor(
    @AccessToken private val accessToken: String,
    private val service: GitHubService,
) : GitHubUserNetworkDataSource {
    override suspend fun searchGitHubUsers(query: String, page: Int): Result<SearchGitHubUserResponse> {
        return getResult { service.searchGitHubUsers(accessToken = accessToken, query = query, page = page) }
    }

    override suspend fun getGitHubUserDetail(username: String): Result<GitHubUserDetailResponse> {
        return getResult { service.getGitHubUserDetail(accessToken = accessToken, username = username) }
    }

    override suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubUserRepoResponse>> {
        return getResult { service.getGitHubUserRepos(accessToken = accessToken, username = username, page = page) }
    }
}
