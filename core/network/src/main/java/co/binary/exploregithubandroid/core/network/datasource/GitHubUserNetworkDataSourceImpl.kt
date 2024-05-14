package co.binary.exploregithubandroid.core.network.datasource

import co.binary.exploregithubandroid.core.common.IoDispatcher
import co.binary.exploregithubandroid.core.network.di.AccessToken
import co.binary.exploregithubandroid.core.network.getResult
import co.binary.exploregithubandroid.core.network.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse
import co.binary.exploregithubandroid.core.network.service.GitHubService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GitHubUserNetworkDataSourceImpl @Inject constructor(
    @AccessToken private val accessToken: String,
    private val service: GitHubService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : GitHubUserNetworkDataSource {
    override suspend fun searchUsers(query: String, page: Int): Result<SearchGitHubUserResponse> =
        withContext(dispatcher) {
            getResult { service.searchUsers(accessToken = accessToken, query = query, page = page) }
        }

    override suspend fun getUserDetail(username: String): Result<GitHubUserDetailResponse> = withContext(dispatcher) {
        getResult { service.getUserDetail(accessToken = accessToken, username = username) }
    }

    override suspend fun getUserRepos(username: String, page: Int): Result<List<GitHubUserRepoResponse>> =
        withContext(dispatcher) {
            getResult { service.getUserRepos(accessToken = accessToken, username = username, page = page) }
        }
}
