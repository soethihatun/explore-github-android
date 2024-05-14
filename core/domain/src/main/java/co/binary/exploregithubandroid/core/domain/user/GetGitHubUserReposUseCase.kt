package co.binary.exploregithubandroid.core.domain.user

import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGitHubUserReposUseCase @Inject constructor(
    private val repository: GitHubUserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(username: String, page: Int): Result<List<GitHubUserRepo>> = withContext(dispatcher) {
        repository.getGitHubUserRepos(username, page)
    }
}
