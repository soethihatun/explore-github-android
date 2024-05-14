package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
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
