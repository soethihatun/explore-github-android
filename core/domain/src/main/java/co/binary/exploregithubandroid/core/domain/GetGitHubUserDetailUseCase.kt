package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import javax.inject.Inject

class GetGitHubUserDetailUseCase @Inject constructor(
    private val repository: GitHubUserRepository,
) {
    suspend operator fun invoke(username: String, page: Int): Result<GitHubUserDetail> {
        return repository.getGitHubUserDetail(username, page)
    }
}
