package co.binary.exploregithubandroid.core.repository

import co.binary.exploregithubandroid.core.model.GitHubRepo
import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.GitHubUserDetail

interface GitHubUserRepository {
    suspend fun searchGitHubUsers(query: String, page: Int): Result<List<GitHubUser>>

    suspend fun getGitHubUserDetail(username: String, page: Int): Result<GitHubUserDetail>

    suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubRepo>>
}
