package co.binary.exploregithubandroid.core.data

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import javax.inject.Inject

internal class GitHubUserRepositoryImpl @Inject constructor() : GitHubUserRepository {
    override suspend fun searchGitHubUsers(query: String): Result<List<GitHubUser>> {
        return Result.success(listOf(GitHubUser(id = 1, login = "johndoe", avatarUrl = "avatarUrl", name = "John Doe")))
    }

    override suspend fun getGitHubUserDetail(login: String): Result<GitHubUserDetail> {
        return Result.success(
            GitHubUserDetail(
                id = 1,
                login = "test",
                avatarUrl = "",
                name = "Test",
                followers = 1,
                following = 1,
                repositories = emptyList(),
            )
        )
    }
}
