package co.binary.exploregithubandroid.core.domain

import co.binary.exploregithubandroid.core.model.GitHubUser
import javax.inject.Inject

class SearchGitHubUserUseCase @Inject constructor() {
    suspend operator fun invoke(query: String): Result<List<GitHubUser>> {
        TODO()
    }
}
