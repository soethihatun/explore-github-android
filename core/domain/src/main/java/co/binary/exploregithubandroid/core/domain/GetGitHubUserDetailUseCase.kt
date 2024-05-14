package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGitHubUserDetailUseCase @Inject constructor(
    private val repository: GitHubUserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(username: String, page: Int): Result<GitHubUserDetail> = withContext(dispatcher) {
        repository.getGitHubUserDetail(username, page)
    }
}
