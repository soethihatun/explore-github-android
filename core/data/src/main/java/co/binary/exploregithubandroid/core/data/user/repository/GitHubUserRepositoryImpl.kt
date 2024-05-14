package co.binary.exploregithubandroid.core.data.user.repository

import co.binary.exploregithubandroid.core.data.user.mapper.asExternalModel
import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo
import co.binary.exploregithubandroid.core.network.user.datasource.GitHubUserNetworkDataSource
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class GitHubUserRepositoryImpl @Inject constructor(
    private val network: GitHubUserNetworkDataSource,
) : GitHubUserRepository {
    override suspend fun searchGitHubUsers(query: String, page: Int): Result<List<GitHubUser>> {
        return network.searchGitHubUsers(query = query, page = page).map { it.asExternalModel() }
    }

    override suspend fun getGitHubUserDetail(username: String, page: Int): Result<GitHubUserDetail> = coroutineScope {
        runCatching {
            val userDeferred = async { network.getGitHubUserDetail(username) }
            val reposDeferred =
                async { getGitHubUserRepos(username, page) }
            val (user, repos) = userDeferred.await() to reposDeferred.await()
            return@coroutineScope user.map { it.asExternalModel(repos = repos.getOrThrow()) }
        }
    }

    override suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubUserRepo>> {
        return network.getGitHubUserRepos(username, page).map { list -> list.map { it.asExternalModel() } }
    }
}
