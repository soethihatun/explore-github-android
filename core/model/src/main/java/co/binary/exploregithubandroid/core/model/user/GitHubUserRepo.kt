package co.binary.exploregithubandroid.core.model.user

data class GitHubUserRepo(
    val id: Int,
    val name: String,
    val description: String?,
    val primaryLanguage: String?,
    val htmlUrl: String,
    val stargazersCount: Int,
)
