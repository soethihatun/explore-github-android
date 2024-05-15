package co.binary.exploregithubandroid.core.domain.user

import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
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
