package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import javax.inject.Inject

class SearchGitHubUsersUseCase @Inject constructor(
    private val repository: GitHubUserRepository,
) {
    suspend operator fun invoke(query: String, page: Int): Result<List<GitHubUser>> {
        return repository.searchGitHubUsers(query = query, page = page)
    }
}
