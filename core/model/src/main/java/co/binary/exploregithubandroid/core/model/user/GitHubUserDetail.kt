package co.binary.exploregithubandroid.core.model.user

data class GitHubUserDetail(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val name: String?,
    val followers: Int,
    val following: Int,
    val repos: List<GitHubUserRepo>
)
