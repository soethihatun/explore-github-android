package co.binary.exploregithubandroid.core.model

data class GitHubUser(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val name: String,
)

// TODO: delete later
val dummyUser = GitHubUser(id = 1, login = "johndoe", avatarUrl = "avatarUrl", name = "John Doe")
