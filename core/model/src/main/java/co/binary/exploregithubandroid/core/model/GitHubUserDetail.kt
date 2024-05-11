package co.binary.exploregithubandroid.core.model

data class GitHubUserDetail(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val name: String,
    val followers: Int,
    val following: Int,
    val repositories: List<GitHubRepository>
)
