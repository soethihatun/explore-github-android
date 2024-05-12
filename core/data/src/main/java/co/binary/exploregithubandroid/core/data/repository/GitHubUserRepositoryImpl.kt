package co.binary.exploregithubandroid.core.data.repository

import co.binary.exploregithubandroid.core.data.mapper.asExternalModel
import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.dummyUserDetail
import co.binary.exploregithubandroid.core.network.datasource.GitHubUserNetworkDataSource
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import javax.inject.Inject

internal class GitHubUserRepositoryImpl @Inject constructor(
    private val network: GitHubUserNetworkDataSource,
) : GitHubUserRepository {
    override suspend fun searchGitHubUsers(query: String): Result<List<GitHubUser>> {
        return network.searchUsers(query).map { it.asExternalModel() }
    }

    override suspend fun getGitHubUserDetail(login: String): Result<GitHubUserDetail> {
        return Result.success(dummyUserDetail)
    }
}
