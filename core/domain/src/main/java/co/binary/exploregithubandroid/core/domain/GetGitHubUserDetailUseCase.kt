package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import javax.inject.Inject

class GetGitHubUserDetailUseCase @Inject constructor() {
    suspend operator fun invoke(login: String): Result<GitHubUserDetail> {
        TODO()
    }
}
