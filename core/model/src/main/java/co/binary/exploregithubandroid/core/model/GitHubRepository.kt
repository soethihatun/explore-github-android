package co.binary.exploregithubandroid.core.model

data class GitHubRepository(
    val id: Int,
    val name: String,
    val description: String,
    val primaryLanguage: String,
    val url: String,
    val stargazersCount: Int,
)
