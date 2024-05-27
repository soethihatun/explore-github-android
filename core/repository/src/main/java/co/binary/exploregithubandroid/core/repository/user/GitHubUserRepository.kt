package co.binary.exploregithubandroid.core.repository.user

import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo

interface GitHubUserRepository {
    suspend fun searchGitHubUsers(query: String, page: Int): Result<List<GitHubUser>>

    suspend fun getGitHubUserDetail(username: String, page: Int): Result<GitHubUserDetail>

    suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubUserRepo>>
}
