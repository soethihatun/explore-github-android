package co.binary.exploregithubandroid.core.data.repository

import co.binary.exploregithubandroid.core.data.mapper.asExternalModel
import co.binary.exploregithubandroid.core.model.GitHubRepo
import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.network.datasource.GitHubUserNetworkDataSource
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class GitHubUserRepositoryImpl @Inject constructor(
    private val network: GitHubUserNetworkDataSource,
) : GitHubUserRepository {
    override suspend fun searchGitHubUsers(query: String, page: Int): Result<List<GitHubUser>> {
        return network.searchUsers(query = query, page = page).map { it.asExternalModel() }
    }

    override suspend fun getGitHubUserDetail(username: String, page: Int): Result<GitHubUserDetail> = coroutineScope {
        runCatching {
            val userDeferred = async { network.getUserDetail(username) }
            val reposDeferred =
                async { getGitHubUserRepos(username, page) }
            val (user, repos) = userDeferred.await() to reposDeferred.await()
            return@coroutineScope user.map { it.asExternalModel(repos = repos.getOrThrow()) }
        }
    }

    override suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubRepo>> {
        return network.getUserRepos(username, page).map { list -> list.map { it.asExternalModel() } }
    }
}
