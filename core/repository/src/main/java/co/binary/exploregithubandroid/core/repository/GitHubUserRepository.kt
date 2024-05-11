package co.binary.exploregithubandroid.core.repository

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail

interface GitHubUserRepository {
    suspend fun searchGitHubUsers(query: String): Result<List<GitHubUser>>

    suspend fun getGitHubUserDetail(login: String): Result<GitHubUserDetail>
}
