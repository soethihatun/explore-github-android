package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubRepo
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import javax.inject.Inject

class GetGitHubUserReposUseCase @Inject constructor(
    private val repository: GitHubUserRepository,
) {
    suspend operator fun invoke(username: String, page: Int): Result<List<GitHubRepo>> {
        return repository.getGitHubUserRepos(username, page)
    }
}
