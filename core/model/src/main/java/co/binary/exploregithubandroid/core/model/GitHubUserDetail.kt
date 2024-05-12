package co.binary.exploregithubandroid.core.model

data class GitHubUserDetail(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val name: String,
    val followers: Int,
    val following: Int,
    val repos: List<GitHubRepository>
)

// TODO: delete later
val dummyUserDetail = GitHubUserDetail(
    id = 1,
    login = "test",
    avatarUrl = "",
    name = "Test",
    followers = 1,
    following = 1,
    repos = emptyList(),
)
