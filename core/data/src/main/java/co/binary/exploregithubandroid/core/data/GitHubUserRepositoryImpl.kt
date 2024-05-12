package co.binary.exploregithubandroid.core.data

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.dummyUserDetail
import co.binary.exploregithubandroid.core.model.dummyUsers
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import javax.inject.Inject

internal class GitHubUserRepositoryImpl @Inject constructor() : GitHubUserRepository {
    override suspend fun searchGitHubUsers(query: String): Result<List<GitHubUser>> {
        return Result.success(dummyUsers)
    }

    override suspend fun getGitHubUserDetail(login: String): Result<GitHubUserDetail> {
        return Result.success(dummyUserDetail)
    }
}
