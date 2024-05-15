package co.binary.exploregithubandroid.core.testing.repository.user

import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import org.jetbrains.annotations.TestOnly

class FakeGitHubUserRepository(
    private val userDetailMap: MutableMap<String, GitHubUserDetail> = mutableMapOf(),
) : GitHubUserRepository {
    override suspend fun searchGitHubUsers(
        query: String,
        page: Int
    ): Result<List<GitHubUser>> {
        if (query.isEmpty() || page < 1) return Result.failure(Exception("Error"))
        return userDetailMap.values.filter { it.username.contains(query, ignoreCase = true) }
            .map {
                GitHubUser(id = it.id, username = it.username, avatarUrl = it.avatarUrl)
            }
            .let { Result.success(it) }
    }

    override suspend fun getGitHubUserDetail(
        username: String,
        page: Int
    ): Result<GitHubUserDetail> {
        if (username.isEmpty() || page < 1) return Result.failure(Exception("Error"))
        return userDetailMap[username]?.let { Result.success(it) } ?: Result.failure(Exception("Error"))
    }

    override suspend fun getGitHubUserRepos(
        username: String,
        page: Int
    ): Result<List<GitHubUserRepo>> {
        if (username.isEmpty() || page < 1) return Result.failure(Exception("Error"))
        return userDetailMap[username]?.repos?.let { Result.success(it) } ?: Result.failure(Exception("Error"))
    }

    @TestOnly
    fun addUserDetail(userDetail: GitHubUserDetail) {
        userDetailMap[userDetail.username] = userDetail
    }

    @TestOnly
    fun clear() {
        userDetailMap.clear()
    }
}
