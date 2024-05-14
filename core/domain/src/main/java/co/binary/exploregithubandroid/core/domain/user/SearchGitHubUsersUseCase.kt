package co.binary.exploregithubandroid.core.domain.user

import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchGitHubUsersUseCase @Inject constructor(
    private val repository: GitHubUserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String, page: Int): Result<List<GitHubUser>> = withContext(dispatcher) {
        repository.searchGitHubUsers(query = query, page = page)
    }
}
