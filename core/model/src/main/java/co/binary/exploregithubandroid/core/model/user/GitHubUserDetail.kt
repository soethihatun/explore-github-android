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

// TODO: delete later
val dummyUserDetail = GitHubUserDetail(
    id = 1,
    username = "johndoe",
    avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
    name = "John Doe",
    followers = 1,
    following = 1,
    repos = dummyRepos,
)

